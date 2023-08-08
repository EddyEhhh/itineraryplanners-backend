package com.scrapheap.itineraryplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.scrapheap.itineraryplanner.dto.ItineraryDetailDTO;
import com.scrapheap.itineraryplanner.dto.TripDetailDTO;
import com.scrapheap.itineraryplanner.exception.ResourceNotFoundException;
import com.scrapheap.itineraryplanner.exception.UnauthorisedResourceAccessException;
import com.scrapheap.itineraryplanner.exception.UnauthorizedException;
import com.scrapheap.itineraryplanner.model.Account;
import com.scrapheap.itineraryplanner.model.Itinerary;
import com.scrapheap.itineraryplanner.model.Trip;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import com.scrapheap.itineraryplanner.repository.TripRepository;
import com.scrapheap.itineraryplanner.s3.S3Buckets;
import com.scrapheap.itineraryplanner.s3.S3Service;
import com.scrapheap.itineraryplanner.util.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private S3Buckets s3Buckets;

//    public List<Trip> getAllTripFromUsername(String username){
//
//
//    }

//    public TripDetailDTO updateTrip(TripDetailDTO){
//
//    }

//    public TripDetailDTO createBasicTrip(TripDetailDTO tripDetailDTO){
//        Trip trip = Trip.builder()
//                .title(tripDetailDTO.getTitle())
//                .location(tripDetailDTO.getLocation())
//                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
//                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
//                .currency(tripDetailDTO.getCurrency())
//                .totalBudget(tripDetailDTO.getTotalBudget())
//                .pictureLink(tripDetailDTO.getPictureLink())
//                .build();
//
//        tripRepository.save(trip);
//        return tripDetailDTO;
//    }

    public List<TripDetailDTO> getUserTrip(){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        return getUserTrip(currentUser);
    }

    public List<TripDetailDTO> getUserTrip(String username){
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        List<Trip> tripList = tripRepository.findByAccount(account, Sort.by(Sort.Order.desc("lastUpdate")));

        tripList = filterByUserAccess(tripList, username);

        return convertToDTOList(tripList);
    }

    public List<TripDetailDTO> getUserUpcomingTrip(String username){
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        List<Trip> tripList = tripRepository.findByAccount(account, Sort.by(Sort.Order.asc("startDate")));

        tripList = filterByUserAccess(tripList, username);

        return convertToDTOList(tripList);
    }

    public TripDetailDTO getTripById(String username, long id){
        Trip trip = tripRepository.findById(id).get();
        log.info("HAS ACCESS: " + username);
        if(hasAccessResource(username, trip)){
            return convertToDTO(trip);
        }
        throw new ResourceNotFoundException("trip.resource.error.notfound");
    }

    public TripDetailDTO createTrip(TripDetailDTO tripDetailDTO){
//        ArrayList<Itinerary> itinerarys = new ArrayList<>();
        Trip trip = convertToEntity(tripDetailDTO);

        generateItinerary(trip);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);

        trip.setAccount(account);

        trip.setLastUpdate(LocalDateTime.now());
        tripRepository.save(trip);
//        tripRepository.save(tripDetailDTO, 1);
        return convertToDTO((trip));
    }

    public Trip generateItinerary(Trip trip){

        long numOfItinerary = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;

        if(trip.getItinerarys() != null && trip.getItinerarys().size() == numOfItinerary){
            return trip;
        }

        if(trip.getItinerarys() == null || trip.getItinerarys().size() == 0){
            List<Itinerary> itineraries = new ArrayList<>();
            for(int itineraryIndex = 0 ; itineraryIndex < numOfItinerary ; itineraryIndex++){
                itineraries.add(new Itinerary());
            }

            trip.setItinerarys(itineraries);
        }else if(trip.getItinerarys().size() < numOfItinerary){
            long numOfItineraryToGenerate = numOfItinerary - trip.getItinerarys().size();
            for(int itineraryIndex = 0 ; itineraryIndex < numOfItineraryToGenerate ; itineraryIndex++){
                trip.getItinerarys().add(new Itinerary());
            }
        }


        log.info("TESTING ----- " + numOfItinerary);
        return trip;
    }

    public TripDetailDTO updateTrip(String username, long id, String tripDetailMap){


        Trip trip = tripRepository.findById(id).get();
//        trip.setTitle("TEST");
//        tripRepository.save(trip);

        if(!hasAccessResource(username, trip)){
            throw new UnauthorisedResourceAccessException();
        }

//        TripDetailDTO tripDetailDTO = convertToDTO(trip);

        try {

            ObjectReader tripReader = objectMapper.readerForUpdating(trip);

            trip = tripReader.readValue(tripDetailMap);


//            trip.setTitle("TEST");

        }catch (Exception e){
            log.info(e.getStackTrace().toString());
        }

        generateItinerary(trip);

        trip.setLastUpdate(LocalDateTime.now());
        tripRepository.save(trip);

//        tripDetailDTO = convertToDTO(tripRepository.findById(id).get());

        return convertToDTO(trip);
    }

    public TripDetailDTO updateTrip(String username, long id, TripDetailDTO tripDetailDTO){


        Trip trip = tripRepository.findById(id).get();

        if(!hasAccessResource(username, trip)){
            throw new UnauthorisedResourceAccessException();
        }


        trip = convertToEntity(tripDetailDTO);
        trip.setId(id);

        generateItinerary(trip);

        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);

        trip.setAccount(account);

        trip.setLastUpdate(LocalDateTime.now());
        tripRepository.save(trip);

//        tripDetailDTO = convertToDTO(tripRepository.findById(id).get());

        return convertToDTO(trip);
    }



    public String uploadTripImage(String username, long id, MultipartFile file)throws IOException {

        if(!hasAccessResource(username)){
            throw new UnauthorizedException("No permission");
        }

        String imageUUID = UUID.randomUUID().toString();
        //puts into aws
        try {
            s3Service.putObject(
                    s3Buckets.getAccount(),
                    "trip-images/%s/%s".formatted(username, imageUUID),
                    file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Trip trip = tripRepository.findById(id).get();
        trip.setPictureLink(imageUUID);
        tripRepository.save(trip);

        return "File uploaded Successfully";
    }

    public String getTripImage(String username, long id) throws IOException{
        Trip trip = tripRepository.findById(id).get();
        if(trip == null || !hasAccessResource(username, trip)){
            throw new UnauthorizedException("No permission");
        }

//        if(account.getImageId().isBlank()){
//            throw new RuntimeException("account profile image not found");
//        }

        String imageUUID = trip.getPictureLink();
        return "https://ip-account.s3.ap-southeast-1.amazonaws.com/trip-images/" + username + "/" + imageUUID;
        // return s3Service.getObject(s3Buckets.getAccount(), "profile-images/%s/%s".formatted(username, profileImageId));
    }


    public String deleteTripImage(String username, long id){
        //TODO: implement this
        if(!hasAccessResource(username)){
            throw new UnauthorizedException("No permission");
        }

        Trip trip = tripRepository.findById(id).get();
        trip.setPictureLink(null);
        tripRepository.save(trip);

        return "Success";
    }


    private boolean hasAccessResource(String username, Trip trip){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.equals(currentUser)){
            return true;
        }
        return trip.isPublic();
    }

    private boolean hasAccessResource(String username){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(currentUser + " " + username);
        return username.equals(currentUser);
    }

    //Filter another user's trip resource when accessed by user of current session
    private List<Trip> filterByUserAccess(List<Trip> tripList, String username){

        if(hasAccessResource(username)){
            return tripList;
        }

        List<Trip>  filteredTrip = new ArrayList<>();

        for(Trip eachTrip : tripList){
            if(eachTrip.isPublic()){
                filteredTrip.add(eachTrip);
            }
        }

        return filteredTrip;

    }

    private List<TripDetailDTO> convertToDTOList(List<Trip> trips){
        List<TripDetailDTO> tripDetailDTOS = new ArrayList<>();

        if(trips == null || trips.size() == 0){
            return null;
        }

        for(Trip trip : trips){
            tripDetailDTOS.add(convertToDTO(trip));
        }

        return tripDetailDTOS;
    }


    private TripDetailDTO convertToDTO(Trip trip){

        if(trip == null){
            return null;
        }

        TripDetailDTO tripDetailDTO = TripDetailDTO.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .location(trip.getLocation())
                .startDate(trip.getStartDate().toString())
                .endDate(trip.getEndDate().toString())
                .currency(trip.getCurrency())
                .totalBudget(trip.getTotalBudget())
                .pictureLink(trip.getPictureLink())
                .lastUpdate(trip.getLastUpdate().toString())
                .build();

        if(trip.getItinerarys() != null && trip.getItinerarys().size() > 0){
            List<ItineraryDetailDTO> itineraryDTOList = itineraryService.convertToDTOList(trip.getItinerarys());
            tripDetailDTO.setItinerarys(itineraryDTOList);
        }

        return tripDetailDTO;
    }

    private Trip convertToEntity(TripDetailDTO tripDetailDTO){
        Trip trip = Trip.builder()
                .title(tripDetailDTO.getTitle())
                .location(tripDetailDTO.getLocation())
                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
                .currency(tripDetailDTO.getCurrency())
                .totalBudget(tripDetailDTO.getTotalBudget())
//                .pictureLink(tripDetailDTO.getPictureLink())
                .isPublic(tripDetailDTO.isPublic())
                .build();



        if(tripDetailDTO.getItinerarys() != null && tripDetailDTO.getItinerarys().size() > 0){
            List<Itinerary> itineraryList = itineraryService.convertToEntityList(tripDetailDTO.getItinerarys());
            trip.setItinerarys(itineraryList);
        }

        return trip;
    }

    public TripDetailDTO deleteTrip(String username, Long id) {
        Trip trip = tripRepository.findById(id).get();
        if (trip == null){
            throw new ResourceNotFoundException("trip.resource.notfound");
        }
        if(!hasAccessResource(username, trip)){
            throw new UnauthorisedResourceAccessException("trip.delete.error.unauthorised");
        }
        Account account = accountRepository.findByUsernameAndIsDeletedFalse(username);
        account.getTrips().remove(trip);
        accountRepository.save(account);

        tripRepository.deleteById(id);

        return convertToDTO(trip);

    }


//    public TripDetailDTO createTrip(TripDetailDTO tripDetailDTO, AccountDetailDTO accountDetailDTO){
////        ArrayList<Itinerary> itinerarys = new ArrayList<>();
//
//        Trip trip = Trip.builder()
//                .title(tripDetailDTO.getTitle())
//                .location(tripDetailDTO.getLocation())
//                .startDate(LocalDateUtil.parseDate(tripDetailDTO.getStartDate()))
//                .endDate(LocalDateUtil.parseDate(tripDetailDTO.getEndDate()))
//                .currency(tripDetailDTO.getCurrency())
//                .totalBudget(tripDetailDTO.getTotalBudget())
//                .pictureLink(tripDetailDTO.getPictureLink())
//                .build();
//
//        if(tripDetailDTO.getItinerary().size() > 0){
//            List<Itinerary> itineraryList = itineraryService.convertToEntityList(tripDetailDTO.getItinerary());
//            trip.setItineraries(itineraryList);
//        }
//
//        Account account = accountRepository.findByUsernameAndIsDeletedFalse(accountDetailDTO.getUsername());
//        trip.setAccount(account);
//
//        tripRepository.save(trip);
//        return tripDetailDTO;
//    }

}

