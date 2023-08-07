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
        Account account = accountRepository.findByUsernameAndIsDeletedFalse("jaq_jw");
        Trip trip1 = generatePredefinedTrip1();
        trip1.setAccount(account);

        Trip trip2 =  generatePredefinedPublicTrip2();
        trip2.setAccount(account);

        Trip trip3 =  generatePredefinedTrip3();
        trip3.setAccount(account);

        tripsToCreate.addAll(List.of(trip1, trip2, trip3));

        tripRepository.saveAll(tripsToCreate);
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
                trip.setLastUpdate(LocalDateTime.now());
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
                trip.setItinerarys(itineraries);

                return trip;
            }

    public Trip generatePredefinedPublicTrip2(){

        Trip trip = new Trip();
        trip.setTitle("Exploring Japan");
        trip.setLocation("Japan, Tokyo");
        trip.setStartDate(LocalDateUtil.parseDate("2023-10-20"));
        trip.setEndDate(LocalDateUtil.parseDate("2023-10-23"));
        trip.setCurrency("JPY");
        trip.setTotalBudget(8000.00);
        trip.setPictureLink("/exploring_japan/image1");
        trip.setPublic(true);
        trip.setLastUpdate(LocalDateTime.now());

        List<Itinerary> itineraries = new ArrayList<>();

        // First Itinerary
        Itinerary firstDayItinerary = new Itinerary();
        firstDayItinerary.setSubheader("Day 1");

        List<Place> firstDayPlaces = new ArrayList<>();

        Place firstDayFirstPlace = new Place();
        firstDayFirstPlace.setLocation("Narita International Airport, Tokyo, Japan");
        firstDayFirstPlace.setTimeStart(LocalTime.parse("08:00:00"));
        firstDayFirstPlace.setTimeEnd(LocalTime.parse("09:00:00"));
        firstDayFirstPlace.setNote("Arrival and immigration");
        firstDayFirstPlace.setPictureLink("/exploring_japan/airport");

        Place firstDaySecondPlace = new Place();
        firstDaySecondPlace.setLocation("Asakusa, Tokyo, Japan");
        firstDaySecondPlace.setTimeStart(LocalTime.parse("10:00:00"));
        firstDaySecondPlace.setTimeEnd(LocalTime.parse("14:00:00"));
        firstDaySecondPlace.setNote("Visit Senso-ji Temple and Nakamise Shopping Street");
        firstDaySecondPlace.setPictureLink("/exploring_japan/asakusa");

        Place firstDayThirdPlace = new Place();
        firstDayThirdPlace.setLocation("Akihabara, Tokyo, Japan");
        firstDayThirdPlace.setTimeStart(LocalTime.parse("15:00:00"));
        firstDayThirdPlace.setTimeEnd(LocalTime.parse("19:00:00"));
        firstDayThirdPlace.setNote("Explore electronics and anime shops");
        firstDayThirdPlace.setPictureLink("/exploring_japan/akihabara");

        firstDayPlaces.add(firstDayFirstPlace);
        firstDayPlaces.add(firstDaySecondPlace);
        firstDayPlaces.add(firstDayThirdPlace);

        firstDayItinerary.setPlaces(firstDayPlaces);
        itineraries.add(firstDayItinerary);

        // Second Itinerary
        Itinerary secondDayItinerary = new Itinerary();
        secondDayItinerary.setSubheader("Day 2");

        List<Place> secondDayPlaces = new ArrayList<>();

        Place secondDayFirstPlace = new Place();
        secondDayFirstPlace.setLocation("Shinjuku Gyoen National Garden, Tokyo, Japan");
        secondDayFirstPlace.setTimeStart(LocalTime.parse("09:30:00"));
        secondDayFirstPlace.setTimeEnd(LocalTime.parse("11:30:00"));
        secondDayFirstPlace.setNote("Enjoy the beautiful garden and cherry blossoms");
        secondDayFirstPlace.setPictureLink("/exploring_japan/shinjuku_gyoen");

        Place secondDaySecondPlace = new Place();
        secondDaySecondPlace.setLocation("Meiji Shrine, Tokyo, Japan");
        secondDaySecondPlace.setTimeStart(LocalTime.parse("12:00:00"));
        secondDaySecondPlace.setTimeEnd(LocalTime.parse("14:00:00"));
        secondDaySecondPlace.setNote("Visit the famous Meiji Shrine");
        secondDaySecondPlace.setPictureLink("/exploring_japan/meiji_shrine");

        Place secondDayThirdPlace = new Place();
        secondDayThirdPlace.setLocation("Harajuku, Tokyo, Japan");
        secondDayThirdPlace.setTimeStart(LocalTime.parse("15:00:00"));
        secondDayThirdPlace.setTimeEnd(LocalTime.parse("18:00:00"));
        secondDayThirdPlace.setNote("Shop and explore Takeshita Street");
        secondDayThirdPlace.setPictureLink("/exploring_japan/harajuku");

        secondDayPlaces.add(secondDayFirstPlace);
        secondDayPlaces.add(secondDaySecondPlace);
        secondDayPlaces.add(secondDayThirdPlace);

        secondDayItinerary.setPlaces(secondDayPlaces);
        itineraries.add(secondDayItinerary);

        // Third Itinerary
        Itinerary thirdDayItinerary = new Itinerary();
        thirdDayItinerary.setSubheader("Day 3");

        List<Place> thirdDayPlaces = new ArrayList<>();

        Place thirdDayFirstPlace = new Place();
        thirdDayFirstPlace.setLocation("Tsukiji Outer Market, Tokyo, Japan");
        thirdDayFirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
        thirdDayFirstPlace.setTimeEnd(LocalTime.parse("11:00:00"));
        thirdDayFirstPlace.setNote("Explore the fresh seafood market");
        thirdDayFirstPlace.setPictureLink("/exploring_japan/tsukiji_market");

        Place thirdDaySecondPlace = new Place();
        thirdDaySecondPlace.setLocation("Imperial Palace, Tokyo, Japan");
        thirdDaySecondPlace.setTimeStart(LocalTime.parse("12:00:00"));
        thirdDaySecondPlace.setTimeEnd(LocalTime.parse("14:00:00"));
        thirdDaySecondPlace.setNote("Visit the historic Imperial Palace");
        thirdDaySecondPlace.setPictureLink("/exploring_japan/imperial_palace");

        Place thirdDayThirdPlace = new Place();
        thirdDayThirdPlace.setLocation("Ueno Park, Tokyo, Japan");
        thirdDayThirdPlace.setTimeStart(LocalTime.parse("15:00:00"));
        thirdDayThirdPlace.setTimeEnd(LocalTime.parse("18:00:00"));
        thirdDayThirdPlace.setNote("Relax and enjoy Ueno Park");
        thirdDayThirdPlace.setPictureLink("/exploring_japan/ueno_park");

        thirdDayPlaces.add(thirdDayFirstPlace);
        thirdDayPlaces.add(thirdDaySecondPlace);
        thirdDayPlaces.add(thirdDayThirdPlace);

        thirdDayItinerary.setPlaces(thirdDayPlaces);
        itineraries.add(thirdDayItinerary);

        // Add more Itineraries for Day 4 and Day 5...

        trip.setItinerarys(itineraries);

        return trip;
    }

    public Trip generatePredefinedTrip3(){

        Trip trip = new Trip();
        trip.setTitle("European Adventure");
        trip.setLocation("Europe");
        trip.setStartDate(LocalDateUtil.parseDate("2023-11-01"));
        trip.setEndDate(LocalDateUtil.parseDate("2023-11-07"));
        trip.setCurrency("EUR");
        trip.setTotalBudget(10000.00);
        trip.setPictureLink("/european_adventure/image1");
        trip.setLastUpdate(LocalDateTime.now());

        List<Itinerary> itineraries = new ArrayList<>();

        // Day 1
        Itinerary day1Itinerary = new Itinerary();
        day1Itinerary.setSubheader("Day 1");

        List<Place> day1Places = new ArrayList<>();

        Place day1FirstPlace = new Place();
        day1FirstPlace.setLocation("Arrival at Paris Charles de Gaulle Airport, France");
        day1FirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
        day1FirstPlace.setTimeEnd(LocalTime.parse("10:00:00"));
        day1FirstPlace.setNote("Arrival and immigration");
        day1FirstPlace.setPictureLink("/european_adventure/paris_airport");

        Place day1SecondPlace = new Place();
        day1SecondPlace.setLocation("Eiffel Tower, Paris, France");
        day1SecondPlace.setTimeStart(LocalTime.parse("11:00:00"));
        day1SecondPlace.setTimeEnd(LocalTime.parse("14:00:00"));
        day1SecondPlace.setNote("Visit the iconic Eiffel Tower");
        day1SecondPlace.setPictureLink("/european_adventure/eiffel_tower");

        Place day1ThirdPlace = new Place();
        day1ThirdPlace.setLocation("Louvre Museum, Paris, France");
        day1ThirdPlace.setTimeStart(LocalTime.parse("15:00:00"));
        day1ThirdPlace.setTimeEnd(LocalTime.parse("18:00:00"));
        day1ThirdPlace.setNote("Explore the famous Louvre Museum");
        day1ThirdPlace.setPictureLink("/european_adventure/louvre_museum");

        day1Places.add(day1FirstPlace);
        day1Places.add(day1SecondPlace);
        day1Places.add(day1ThirdPlace);

        day1Itinerary.setPlaces(day1Places);
        itineraries.add(day1Itinerary);

        // Day 2
        Itinerary day2Itinerary = new Itinerary();
        day2Itinerary.setSubheader("Day 2");

        List<Place> day2Places = new ArrayList<>();

        Place day2FirstPlace = new Place();
        day2FirstPlace.setLocation("Versailles Palace, France");
        day2FirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
        day2FirstPlace.setTimeEnd(LocalTime.parse("12:00:00"));
        day2FirstPlace.setNote("Visit the magnificent Versailles Palace");
        day2FirstPlace.setPictureLink("/european_adventure/versailles_palace");

        Place day2SecondPlace = new Place();
        day2SecondPlace.setLocation("Montmartre, Paris, France");
        day2SecondPlace.setTimeStart(LocalTime.parse("13:00:00"));
        day2SecondPlace.setTimeEnd(LocalTime.parse("16:00:00"));
        day2SecondPlace.setNote("Explore the artsy neighborhood of Montmartre");
        day2SecondPlace.setPictureLink("/european_adventure/montmartre");

        day2Places.add(day2FirstPlace);
        day2Places.add(day2SecondPlace);

        day2Itinerary.setPlaces(day2Places);
        itineraries.add(day2Itinerary);

        // Day 3
        Itinerary day3Itinerary = new Itinerary();
        day3Itinerary.setSubheader("Day 3");

        List<Place> day3Places = new ArrayList<>();

        Place day3FirstPlace = new Place();
        day3FirstPlace.setLocation("Colosseum, Rome, Italy");
        day3FirstPlace.setTimeStart(LocalTime.parse("10:00:00"));
        day3FirstPlace.setTimeEnd(LocalTime.parse("14:00:00"));
        day3FirstPlace.setNote("Visit the ancient Colosseum");
        day3FirstPlace.setPictureLink("/european_adventure/colosseum");

        Place day3SecondPlace = new Place();
        day3SecondPlace.setLocation("Roman Forum, Rome, Italy");
        day3SecondPlace.setTimeStart(LocalTime.parse("15:00:00"));
        day3SecondPlace.setTimeEnd(LocalTime.parse("18:00:00"));
        day3SecondPlace.setNote("Explore the historic Roman Forum");
        day3SecondPlace.setPictureLink("/european_adventure/roman_forum");

        day3Places.add(day3FirstPlace);
        day3Places.add(day3SecondPlace);

        day3Itinerary.setPlaces(day3Places);
        itineraries.add(day3Itinerary);

        // ... (Existing code for Day 1 to Day 3)

// Day 4
        Itinerary day4Itinerary = new Itinerary();
        day4Itinerary.setSubheader("Day 4");

        List<Place> day4Places = new ArrayList<>();

        Place day4FirstPlace = new Place();
        day4FirstPlace.setLocation("Sagrada Familia, Barcelona, Spain");
        day4FirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
        day4FirstPlace.setTimeEnd(LocalTime.parse("12:00:00"));
        day4FirstPlace.setNote("Visit the iconic Sagrada Familia");
        day4FirstPlace.setPictureLink("/european_adventure/sagrada_familia");

        Place day4SecondPlace = new Place();
        day4SecondPlace.setLocation("Park Güell, Barcelona, Spain");
        day4SecondPlace.setTimeStart(LocalTime.parse("13:00:00"));
        day4SecondPlace.setTimeEnd(LocalTime.parse("16:00:00"));
        day4SecondPlace.setNote("Explore the beautiful Park Güell");
        day4SecondPlace.setPictureLink("/european_adventure/park_guell");

        day4Places.add(day4FirstPlace);
        day4Places.add(day4SecondPlace);

        day4Itinerary.setPlaces(day4Places);
        itineraries.add(day4Itinerary);

// Day 5
        Itinerary day5Itinerary = new Itinerary();
        day5Itinerary.setSubheader("Day 5");

        List<Place> day5Places = new ArrayList<>();

        Place day5FirstPlace = new Place();
        day5FirstPlace.setLocation("Amsterdam Canals, Netherlands");
        day5FirstPlace.setTimeStart(LocalTime.parse("10:00:00"));
        day5FirstPlace.setTimeEnd(LocalTime.parse("14:00:00"));
        day5FirstPlace.setNote("Explore the charming Amsterdam canals");
        day5FirstPlace.setPictureLink("/european_adventure/amsterdam_canals");

        Place day5SecondPlace = new Place();
        day5SecondPlace.setLocation("Van Gogh Museum, Amsterdam, Netherlands");
        day5SecondPlace.setTimeStart(LocalTime.parse("15:00:00"));
        day5SecondPlace.setTimeEnd(LocalTime.parse("18:00:00"));
        day5SecondPlace.setNote("Visit the famous Van Gogh Museum");
        day5SecondPlace.setPictureLink("/european_adventure/van_gogh_museum");

        day5Places.add(day5FirstPlace);
        day5Places.add(day5SecondPlace);

        day5Itinerary.setPlaces(day5Places);
        itineraries.add(day5Itinerary);

// Day 6
        Itinerary day6Itinerary = new Itinerary();
        day6Itinerary.setSubheader("Day 6");

        List<Place> day6Places = new ArrayList<>();

        Place day6FirstPlace = new Place();
        day6FirstPlace.setLocation("Neuschwanstein Castle, Germany");
        day6FirstPlace.setTimeStart(LocalTime.parse("09:00:00"));
        day6FirstPlace.setTimeEnd(LocalTime.parse("12:00:00"));
        day6FirstPlace.setNote("Visit the fairytale-like Neuschwanstein Castle");
        day6FirstPlace.setPictureLink("/european_adventure/neuschwanstein_castle");

        Place day6SecondPlace = new Place();
        day6SecondPlace.setLocation("Marienplatz, Munich, Germany");
        day6SecondPlace.setTimeStart(LocalTime.parse("13:00:00"));
        day6SecondPlace.setTimeEnd(LocalTime.parse("16:00:00"));
        day6SecondPlace.setNote("Explore the vibrant Marienplatz in Munich");
        day6SecondPlace.setPictureLink("/european_adventure/marienplatz");

        day6Places.add(day6FirstPlace);
        day6Places.add(day6SecondPlace);

        day6Itinerary.setPlaces(day6Places);
        itineraries.add(day6Itinerary);

// Day 7
        Itinerary day7Itinerary = new Itinerary();
        day7Itinerary.setSubheader("Day 7");

        List<Place> day7Places = new ArrayList<>();

        Place day7FirstPlace = new Place();
        day7FirstPlace.setLocation("Charles Bridge, Prague, Czech Republic");
        day7FirstPlace.setTimeStart(LocalTime.parse("10:00:00"));
        day7FirstPlace.setTimeEnd(LocalTime.parse("14:00:00"));
        day7FirstPlace.setNote("Walk across the historic Charles Bridge");
        day7FirstPlace.setPictureLink("/european_adventure/charles_bridge");

        Place day7SecondPlace = new Place();
        day7SecondPlace.setLocation("Prague Castle, Czech Republic");
        day7SecondPlace.setTimeStart(LocalTime.parse("15:00:00"));
        day7SecondPlace.setTimeEnd(LocalTime.parse("18:00:00"));
        day7SecondPlace.setNote("Visit the majestic Prague Castle");
        day7SecondPlace.setPictureLink("/european_adventure/prague_castle");

        day7Places.add(day7FirstPlace);
        day7Places.add(day7SecondPlace);

        day7Itinerary.setPlaces(day7Places);
        itineraries.add(day7Itinerary);

        trip.setItinerarys(itineraries);

        return trip;

    }


}



