package com.tracking.courier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.tracking.courier.Exception.CourierNotFoundException;
import com.tracking.courier.model.Courier;
import com.tracking.courier.model.Location;
import com.tracking.courier.model.Store;
import com.tracking.courier.repository.CourierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracking.courier.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CourierApplication.class)
public class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @InjectMocks
    private CourierService courierService;

    @Autowired
    private ObjectMapper objectMapper;

    private Courier courier1;
    private Courier courier2;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        Location location1 = new Location(40.994445, 29.125622);
        Store store1 = new Store("store1", 40.994445, 29.125622);

        courier1 = new Courier();
        courier1.setId("courier1");
        courier1.setName("John Doe");
        courier1.setLastKnownLocation(location1);
        courier1.setLastSeenAt(store1);

        Location location2 = new Location(40.986106, 29.1161293);
        Store store2 = new Store("store2", 40.986106, 29.1161293);

        courier2 = new Courier();
        courier2.setId("courier2");
        courier2.setName("Jane Smith");
        courier2.setLastKnownLocation(location2);
        courier2.setLastSeenAt(store2);

        // add some sample couriers to the service
        Courier courier1 = new Courier("1", 0, "John Doe");
        courier1.setLastKnownLocation(new Location(37.7749, -122.4194));
        courierService.saveCourier(courier1);

        Courier courier2 = new Courier("2", 0, "Jane Smith");
        courier2.setLastKnownLocation(new Location(37.7749, -122.4194));
        courierService.saveCourier(courier2);
    }

    @Test
    public void testFindAllCouriers() {
        when(courierRepository.findAll()).thenReturn(Arrays.asList(courier1, courier2));
        List<Courier> couriers = courierService.findAllCouriers();
        assertEquals(2, couriers.size());
        verify(courierRepository).findAll();
    }

    @Test
    public void testFindCourierById() {
        when(courierRepository.findById(new Long(String.valueOf(courier1)))).thenReturn(Optional.of(courier1));
        Courier courier = courierService.findCourierById(new Long(String.valueOf(courier1)));
        assertEquals("John Doe", courier.getName());
        verify(courierRepository).findById(new Long(String.valueOf(courier1)));

        assertThrows(CourierNotFoundException.class, () -> {
            courierService.findCourierById(new Long(null));
        });
    }

    @Test
    public void testFindCouriersByLastKnownLocationNear() {
        Location location = new Location(40.992, 29.123);
        when(courierRepository.findCouriersByLastKnownLocationNear(location, 1.0))
                .thenReturn(Arrays.asList(courier1));
        Location location2 = new Location(40.992, 29.123);
        List<Courier> couriers = courierService.findCouriersByLastKnownLocationNear(location2, 1.0);
        assertEquals(1, couriers.size());
        verify(courierRepository).findCouriersByLastKnownLocationNear(40.992, 29.123, 1.0);
    }

    @Test
    void getCourierById_shouldReturnCourier_whenCourierExists() throws CourierNotFoundException {
        // Arrange
        Courier expectedCourier = new Courier();
        expectedCourier.setId("123");
        when(courierRepository.findById(any(String.class))).thenReturn(Optional.of(expectedCourier));

        // Act
        Courier actualCourier = courierService.findCourierById(new Long(123));

        // Assert
        assertNotNull(actualCourier);
        assertEquals(expectedCourier.getId(), actualCourier.getId());
        verify(courierRepository, times(1)).findById("123");
    }

    @Test
    void getCourierById_shouldThrowCourierNotFoundException_whenCourierDoesNotExist() {
        // Arrange
        when(courierRepository.findById(any(String.class))).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CourierNotFoundException.class, () -> courierService.findCourierById(new Long(123));
        verify(courierRepository, times(1)).findById("123");
    }

    @Test
    void updateCourierLocation_shouldUpdateCourierLocation_whenCourierExists() throws CourierNotFoundException {
        // Arrange
        Courier expectedCourier = new Courier();
        expectedCourier.setId("123");
        expectedCourier.setLastKnownLocation(new Location(12.34, 56.78));
        when(courierRepository.findById(any(String.class))).thenReturn(Optional.of(expectedCourier));

        // Act
        courierService.updateCourierLocation(new Long(123), new Location(90.12, 34.56));

        // Assert
        assertEquals(90.12, expectedCourier.getLastKnownLocation().getLatitude());
        assertEquals(34.56, expectedCourier.getLastKnownLocation().getLongitude());
        verify(courierRepository, times(1)).findById("123");
        verify(courierRepository, times(1)).save(expectedCourier);
    }

    @Test
    void updateCourierLocation_shouldThrowCourierNotFoundException_whenCourierDoesNotExist() {
        // Arrange
        when(courierRepository.findById(any(String.class))).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CourierNotFoundException.class, () -> courierService.updateCourierLocation("123", new Location(90.12, 34.56)));
        verify(courierRepository, times(1)).findById("123");
        verify(courierRepository, times(0)).save(any(Courier.class));
    }

}