package com.scrapheap.itineraryplanner.config;

import com.scrapheap.itineraryplanner.model.*;
import com.scrapheap.itineraryplanner.repository.AccountRepository;
import com.scrapheap.itineraryplanner.repository.TripRepository;
import com.scrapheap.itineraryplanner.util.LocalDateTimeUtil;
import com.scrapheap.itineraryplanner.util.LocalDateUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PopulateSampleData {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TripRepository tripRepository;


//    @PostConstruct
//    private void postConstruct(AccountRepository accountRepository) {
////        log.info("Test----------------------");
//        autoGenerateAccount(accountRepository);
//        log.info(accountRepository.findByUsernameAndIsDeletedFalse("jaq_jw").toString());
////        log.info("After----------------------");
//    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {

        autoGenerateAccount();
        log.info(accountRepository.findByUsernameAndIsDeletedFalse("jaq_jw").toString());
        autoGenerateTrip();
    }

    public void autoGenerateAccount(){
        List<Account> accountToCreate = new ArrayList<Account>();

        accountToCreate.add(
                generateAccount(
                        "Leo",
                        "Leo@example.com",
                        "leo_x",
                        "Password1!"));
        accountToCreate.add(
                generateAccount(
                        "Jia Wei",
                        "jw@example.com",
                        "jaq_jw",
                        "Password1!"));
        accountToCreate.add(
                generateAccount(
                        "Ash",
                        "ashley@example.com",
                        "ashley",
                        "Password1!"));
        accountToCreate.add(
                generateAccount(
                        "Fang",
                        "Fangwww@example.com",
                        "fangwww",
                        "Password1!"));
        accountToCreate.add(
                generateAccount(
                        "Clar",
                        "Clarissa@example.com",
                        "clarissa",
                        "Password1!"));
        accountToCreate.add(
                generateAccount(
                        "John Doe",
                        "John@example.com",
                        "johndoe",
                        "Password1!"));

        accountRepository.saveAllAndFlush(accountToCreate);
    }

    public void autoGenerateTrip(){

        List<Trip> tripsToCreate = new ArrayList<Trip>();
        //jaq_jw
        Trip trip1 = generatePredefinedTrip1();
//        trip1.setAccount(accountRepository.findByUsernameAndIsDeletedFalse("jaq_jw"));
        tripsToCreate.add(trip1);


        tripRepository.save(trip1);
    }


    public Account generateAccount(String displayName, String email, String username,  String password){
        LocalDateTime currentTime = LocalDateTime.now();

        LoginAttempt loginAttempt = LoginAttempt.builder().
                numberOfAttempts(0).
                lastAttemptTimestamp(currentTime).
                build();

        Setting setting = Setting.builder().
                language("en").
                theme("light").
                build();

        String defaultRole = "USER";

//            ForgotPassword forgotPassword1 = ForgotPassword.builder().
////                    token().
////                    timestamp().
//                    build();
//
//            Session session1 = Session.builder().
////                    token().
////                    timestamp().
//                    build();

        Account account = Account.builder().
                displayName(displayName).
                email(email).
                username(username).
                password(passwordEncoder.encode(password)).
                created(currentTime).
                loginAttempt(loginAttempt).
                setting(setting).
                role(defaultRole).
//                    forgotPassword(forgotPassword1).
//                    session(session1).
                build();

        return account;
    }

    public Trip generatePredefinedTrip1(){

                // Create the Trip object
                Trip trip = new Trip();
                trip.setTitle("Beach Vacation in Bali");
                trip.setLocation("Bali, Indonesia");
                trip.setStartDate(LocalDateUtil.parseDate("2023-08-10"));
                trip.setEndDate(LocalDateUtil.parseDate("2023-08-13"));
                trip.setCurrency("USD");
                trip.setTotalBudget(5000.00);
                trip.setPictureLink("/beach_vacation_bali/image1");

                // Create the list of Itinerary objects
                List<Itinerary> itineraries = new ArrayList<>();

                // Create the first Itinerary object
                Itinerary firstDayItinerary = new Itinerary();
                firstDayItinerary.setSubheader("First day!");

                // Create the list of Place objects for the first Itinerary
                List<Place> firstDayPlaces = new ArrayList<>();

                // Create the first Place object for the first Itinerary
                Place firstDayFirstPlace = new Place();
                firstDayFirstPlace.setLocation("Ngurah Rai International Airport, Denpasar, Bali, Indonesia");
                firstDayFirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
                firstDayFirstPlace.setTimeEnd(LocalTime.parse("10:00:00"));
                firstDayFirstPlace.setNote("Arrival and pick up rental car");
                firstDayFirstPlace.setPictureLink("/beach_vacation_bali/airport");

                // Create the Flight object for the first Place
                Flight firstDayFirstPlaceFlight = new Flight();
                firstDayFirstPlaceFlight.setAirline("GA");
                firstDayFirstPlaceFlight.setFlightNumber(123);
                firstDayFirstPlaceFlight.setDepartureDate(LocalDateTime.parse("2023-08-10T09:00:00"));
                firstDayFirstPlaceFlight.setArrivalDate(LocalDateTime.parse("2023-08-10T10:00:00"));

                // Set the Flight object for the first Place
                firstDayFirstPlace.setFlight(firstDayFirstPlaceFlight);

                // Add the first Place to the list of Places for the first Itinerary
                firstDayPlaces.add(firstDayFirstPlace);

                // Create the second Place object for the first Itinerary
                Place firstDaySecondPlace = new Place();
                firstDaySecondPlace.setLocation("Seminyak, Bali, Indonesia");
                firstDaySecondPlace.setTimeStart(LocalTime.parse("11:00:00"));
                firstDaySecondPlace.setTimeEnd(LocalTime.parse("14:00:00"));
                firstDaySecondPlace.setNote("Check-in to beachfront villa");
                firstDaySecondPlace.setPictureLink("/beach_vacation_bali/villa");

                // Create the Accommodation object for the second Place
                Accommodation firstDaySecondPlaceAccommodation = new Accommodation();
                firstDaySecondPlaceAccommodation.setAddress("Jl. Kayu Aya, Seminyak Beach");
                firstDaySecondPlaceAccommodation.setCheckIn(LocalDateTime.parse("2023-08-10T14:00:00"));
                firstDaySecondPlaceAccommodation.setCheckOut(LocalDateTime.parse("2023-08-17T12:00:00"));

                // Set the Accommodation object for the second Place
                firstDaySecondPlace.setAccommodation(firstDaySecondPlaceAccommodation);

                // Add the second Place to the list of Places for the first Itinerary
                firstDayPlaces.add(firstDaySecondPlace);

                // Create the third Place object for the first Itinerary
                Place firstDayThirdPlace = new Place();
                firstDayThirdPlace.setLocation("Double Six Beach, Seminyak, Bali, Indonesia");
                firstDayThirdPlace.setTimeStart(LocalTime.parse("15:00:00"));
                firstDayThirdPlace.setTimeEnd(LocalTime.parse("18:00:00"));
                firstDayThirdPlace.setNote("Relax on the beach and enjoy sunset");
                firstDayThirdPlace.setPictureLink("/beach_vacation_bali/beach");

                // Add the third Place to the list of Places for the first Itinerary
                firstDayPlaces.add(firstDayThirdPlace);

                // Set the list of Places for the first Itinerary
                firstDayItinerary.setPlaces(firstDayPlaces);

                // Add the first Itinerary to the list of Itineraries
                itineraries.add(firstDayItinerary);

                // Create the second Itinerary object
                Itinerary secondDayItinerary = new Itinerary();
                secondDayItinerary.setSubheader("Second day!");

                // Create the list of Place objects for the second Itinerary
                List<Place> secondDayPlaces = new ArrayList<>();

                // Create the first Place object for the second Itinerary
                Place secondDayFirstPlace = new Place();
                secondDayFirstPlace.setLocation("Ubud, Bali, Indonesia");
                secondDayFirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
                secondDayFirstPlace.setTimeEnd(LocalTime.parse("13:00:00"));
                secondDayFirstPlace.setNote("Visit Ubud Monkey Forest and explore local markets");
                secondDayFirstPlace.setPictureLink("/beach_vacation_bali/ubud");

                // Add the first Place to the list of Places for the second Itinerary
                secondDayPlaces.add(secondDayFirstPlace);

                // Create the second Place object for the second Itinerary
                Place secondDaySecondPlace = new Place();
                secondDaySecondPlace.setLocation("Tegallalang Rice Terraces, Ubud, Bali, Indonesia");
                secondDaySecondPlace.setTimeStart(LocalTime.parse("14:00:00"));
                secondDaySecondPlace.setTimeEnd(LocalTime.parse("16:00:00"));
                secondDaySecondPlace.setNote("Take a walk through the beautiful rice terraces");
                secondDaySecondPlace.setPictureLink("/beach_vacation_bali/rice_terrace");

                // Add the second Place to the list of Places for the second Itinerary
                secondDayPlaces.add(secondDaySecondPlace);

                // Create the third Place object for the second Itinerary
                Place secondDayThirdPlace = new Place();
                secondDayThirdPlace.setLocation("Ubud Palace, Ubud, Bali, Indonesia");
                secondDayThirdPlace.setTimeStart(LocalTime.parse("17:00:00"));
                secondDayThirdPlace.setTimeEnd(LocalTime.parse("19:00:00"));
                secondDayThirdPlace.setNote("Watch traditional Balinese dance performance");
                secondDayThirdPlace.setPictureLink("/beach_vacation_bali/ubud_palace");

                // Add the third Place to the list of Places for the second Itinerary
                secondDayPlaces.add(secondDayThirdPlace);

                // Set the list of Places for the second Itinerary
                secondDayItinerary.setPlaces(secondDayPlaces);

                // Add the second Itinerary to the list of Itineraries
                itineraries.add(secondDayItinerary);

                // Create the third Itinerary object
                Itinerary thirdDayItinerary = new Itinerary();
                thirdDayItinerary.setSubheader("Third day!");

                // Create the list of Place objects for the third Itinerary
                List<Place> thirdDayPlaces = new ArrayList<>();

                // Create the first Place object for the third Itinerary
                Place thirdDayFirstPlace = new Place();
                thirdDayFirstPlace.setLocation("Nusa Dua, Bali, Indonesia");
                thirdDayFirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
                thirdDayFirstPlace.setTimeEnd(LocalTime.parse("12:00:00"));
                thirdDayFirstPlace.setNote("Relax at Nusa Dua Beach and enjoy water activities");
                thirdDayFirstPlace.setPictureLink("/beach_vacation_bali/nusa_dua");

                // Add the first Place to the list of Places for the third Itinerary
                thirdDayPlaces.add(thirdDayFirstPlace);

                // Create the second Place object for the third Itinerary
                Place thirdDaySecondPlace = new Place();
                thirdDaySecondPlace.setLocation("Waterblow, Nusa Dua, Bali, Indonesia");
                thirdDaySecondPlace.setTimeStart(LocalTime.parse("13:00:00"));
                thirdDaySecondPlace.setTimeEnd(LocalTime.parse("15:00:00"));
                thirdDaySecondPlace.setNote("Experience the powerful waves at Waterblow");
                thirdDaySecondPlace.setPictureLink("/beach_vacation_bali/waterblow");

                // Add the second Place to the list of Places for the third Itinerary
                thirdDayPlaces.add(thirdDaySecondPlace);

                // Create the third Place object for the third Itinerary
                Place thirdDayThirdPlace = new Place();
                thirdDayThirdPlace.setLocation("Geger Beach, Nusa Dua, Bali, Indonesia");
                thirdDayThirdPlace.setTimeStart(LocalTime.parse("16:00:00"));
                thirdDayThirdPlace.setTimeEnd(LocalTime.parse("18:00:00"));
                thirdDayThirdPlace.setNote("Enjoy a peaceful sunset at Geger Beach");
                thirdDayThirdPlace.setPictureLink("/beach_vacation_bali/geger_beach");

                // Add the third Place to the list of Places for the third Itinerary
                thirdDayPlaces.add(thirdDayThirdPlace);

                // Set the list of Places for the third Itinerary
                thirdDayItinerary.setPlaces(thirdDayPlaces);

                // Add the third Itinerary to the list of Itineraries
                itineraries.add(thirdDayItinerary);

                // Set the list of Itineraries for the Trip
                trip.setItineraries(itineraries);

                return trip;
            }




}



