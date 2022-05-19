package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false); //Create a temp parking spot
            Ticket ticket = new Ticket(); //Create a temp ticket
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000))); //def the time limit of the ticket
            ticket.setParkingSpot(parkingSpot); //def "parkingSpot" of the ticket
            ticket.setVehicleRegNumber("ABCDEF"); //def the number of the vehicle

            when(ticketDAO.getTicket(anyString())).thenReturn(ticket); //return the temp ticker when any "getTicket" is called
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true); //return true when an update of any ticket is make
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true); //return true whan an update of any parcking slot is update

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO); //Def parkingService (locate: here)
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest(){
        parkingService.processExitingVehicle(); //process to the exiciting of the vehicle to the parking slot define in the "setUpPerTest" method
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

    }

}
