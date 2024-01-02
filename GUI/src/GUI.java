import jdk.internal.org.objectweb.asm.tree.InsnList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

class Hotel {
    private String name;
    private String address;
    private String contactDetails;
    private int roomNumber;
    private ArrayList<Room> rooms;

    public Hotel(String name, String address, String contactDetails) {
        this.name = name;
        this.address = address;
        this.contactDetails = contactDetails;
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public boolean checkAvailability(int roomNumber, Date startDate, Date endDate) {
        // Implementation to check room availability for the given date range
        Room room = findRoomByNumber(roomNumber);
        if (room != null) {
            return room.isAvailable(startDate, endDate);
        }
        return false;
    }

    public void bookRoom(int roomNumber, Customer customer, Date startDate, Date endDate) {
        // Implementation to book a room for a customer and update its availability status
        Room room = findRoomByNumber(roomNumber);
        if (room != null && room.isAvailable(startDate, endDate)) {
            room.bookRoom(customer, startDate, endDate);
        } else {
            System.out.println("Room not available for the specified date range.");
        }
    }


    public void checkOut(int roomNumber) {
        Room room = findRoomByNumber(roomNumber);
        if (room != null && room.isOccupied()) {
            room.checkOut();
        } else {
            System.out.println("Room is not occupied or does not exist.");
        }
    }


    public int getRoomNumber() {
        return roomNumber;
    }

    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public ArrayList<String> getRoomDetailsList() {
        ArrayList<String> roomDetailsList = new ArrayList<>();

        for (Room room : rooms) {
            roomDetailsList.add(room.getRoomDetails());
        }

        return roomDetailsList;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}

class Room {
    private int roomNumber;
    private String type;
    private double price;
    private boolean availability;
    private Customer customer;

    public Room(int roomNumber, String type, double price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.availability = true;
        this.customer = null;
    }

    public void updateAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getRoomDetails() {
        // Return room details as a string
        return "Room Number: " + roomNumber + "\nType: " + type + "\nPrice: " + price + "\nAvailability: " + availability;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isAvailable(Date startDate, Date endDate) {
        if (availability) {
            // Check if the room is available for the given date range
            if (customer == null) {
                // If there is no customer, the room is available
                return true;
            } else {
                // If there is a customer, check if the date range overlaps with the existing booking
                boolean isBeforeExistingBooking = endDate.before(customer.getCheckInDate()) || endDate.equals(customer.getCheckInDate());
                boolean isAfterExistingBooking = startDate.after(customer.getCheckOutDate()) || startDate.equals(customer.getCheckOutDate());

                return isBeforeExistingBooking || isAfterExistingBooking;
            }
        }
        return false;
    }

    public void bookRoom(Customer customer, Date startDate, Date endDate) {
        if (isAvailable(startDate, endDate)) {
            // Set the customer and update availability
            this.customer = customer;
            this.availability = false;

            // Update Customer check-in and check-out dates
            customer.checkIn(this.roomNumber, startDate);
            customer.checkOut(endDate);


            System.out.println("Room booked successfully!");
        } else {
            System.out.println("Room not available for the specified date range.");
        }
    }

    public void checkOut() {
        if (isOccupied()) {
            // Perform any necessary actions related to checking out a customer
            this.customer = null;
            this.availability = true;

            System.out.println("Room checked out successfully!");
        } else {
            System.out.println("Room is not occupied.");
        }
    }

    public boolean isOccupied() {

        return customer != null;
    }
    public Customer getCustomer() {

        return customer;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }


}


class Customer {
    private String name;
    private String contactDetails;
    private Date checkInDate;
    private Date checkOutDate;
    private int roomNumber;

    public Customer(String name, String contactDetails) {
        this.name = name;
        this.contactDetails = contactDetails;
    }

    public void checkIn(int roomNumber, Date checkInDate) {
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
    }

    public void checkOut(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public String getName() {
        return name;
    }

    public String getContactDetails() {
        return contactDetails;
    }
}

class HotelManagementGUI {
    private Hotel hotel;

    public HotelManagementGUI(Hotel hotel) {
        this.hotel = hotel;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);

        JLabel text = new JLabel("Welcome To Booking System");

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1000, 10));

        JButton addRoomButton = new JButton("Add Room");
        JButton removeRoomButton = new JButton("Remove Room");
        JButton checkAvailabilityButton = new JButton("Check Availability");
        JButton bookRoomButton = new JButton("Book Room");
        JButton checkOutButton = new JButton("Check Out");
        JButton viewAllRoomsButton = new JButton("View All Rooms");
        JButton viewBookingButton = new JButton("View Booking Room");

        Dimension buttonSize = new Dimension(150, 30);
        addRoomButton.setPreferredSize(buttonSize);
        removeRoomButton.setPreferredSize(buttonSize);
        checkAvailabilityButton.setPreferredSize(buttonSize);
        bookRoomButton.setPreferredSize(buttonSize);
        checkOutButton.setPreferredSize(buttonSize);
        viewAllRoomsButton.setPreferredSize(buttonSize);

        inputPanel = new JPanel(new GridLayout(0, 2));

        viewAllRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Room> allRooms = hotel.getRooms();

                if (!allRooms.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String[][] data = new String[allRooms.size()][4];
                    int i = 0;

                    for (Room room : allRooms) {
                        data[i][0] = String.valueOf(room.getRoomNumber());
                        data[i][1] = room.getType();
                        data[i][2] = String.valueOf(room.getPrice());
                        data[i][3] = room.isAvailable(new Date(), new Date()) ? "Available" : "Booked";

                        i++;
                    }

                    String[] columnNames = {"Room Number", "Type", "Price", "Availability"};

                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);

                    JOptionPane.showMessageDialog(null, scrollPane, "All Rooms Information", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No rooms available.");
                }
            }
        });

        addRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for adding a room
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Room Number:"));
                JTextField roomNumberField = new JTextField();
                inputPanel.add(roomNumberField);
                inputPanel.add(new JLabel("Room Type:"));
                JTextField roomTypeField = new JTextField();
                inputPanel.add(roomTypeField);
                inputPanel.add(new JLabel("Room Price:"));
                JTextField roomPriceField = new JTextField();
                inputPanel.add(roomPriceField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int roomNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter Room Number:"));
                        String type = JOptionPane.showInputDialog("Enter Room Type:");
                        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Room Price:"));

                        Room newRoom = new Room(roomNumber, type, price);
                        hotel.addRoom(newRoom);
                        JOptionPane.showMessageDialog(null, "Room added successfully!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                    }
                }
            }
        });

        removeRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for removing a room
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Room Number to remove:"));
                JTextField roomNumberField = new JTextField();
                inputPanel.add(roomNumberField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int roomNumber = Integer.parseInt(roomNumberField.getText());
                        Room roomToRemove = hotel.findRoomByNumber(roomNumber);

                        if (roomToRemove != null) {
                            hotel.removeRoom(roomToRemove);
                            JOptionPane.showMessageDialog(null, "Room removed successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Room not found!");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                    }
                }
            }
        });

         checkAvailabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for checking room availability
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Enter Room Number:"));
                JTextField roomNumberField = new JTextField();
                inputPanel.add(roomNumberField);
                inputPanel.add(new JLabel("Enter Start Date (yyyy-MM-dd):"));
                JTextField startDateField = new JTextField();
                inputPanel.add(startDateField);
                inputPanel.add(new JLabel("Enter End Date (yyyy-MM-dd):"));
                JTextField endDateField = new JTextField();
                inputPanel.add(endDateField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int roomNumber = Integer.parseInt(roomNumberField.getText());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = dateFormat.parse(startDateField.getText());
                        Date endDate = dateFormat.parse(endDateField.getText());

                        boolean isAvailable = hotel.checkAvailability(roomNumber, startDate, endDate);

                        if (isAvailable) {
                            JOptionPane.showMessageDialog(null, "Room is available for the given date range.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Room is not available for the given date range.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input or date format!");
                    }
                }
            }
        });

        bookRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for booking a room
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Enter Room Number:"));
                JTextField roomNumberField = new JTextField();
                inputPanel.add(roomNumberField);
                inputPanel.add(new JLabel("Enter Customer Name:"));
                JTextField customerNameField = new JTextField();
                inputPanel.add(customerNameField);
                inputPanel.add(new JLabel("Enter Customer Contact Details:"));
                JTextField contactDetailsField = new JTextField();
                inputPanel.add(contactDetailsField);
                inputPanel.add(new JLabel("Enter Check-in Date (yyyy-MM-dd):"));
                JTextField checkInDateField = new JTextField();
                inputPanel.add(checkInDateField);
                inputPanel.add(new JLabel("Enter Check-out Date (yyyy-MM-dd):"));
                JTextField checkOutDateField = new JTextField();
                inputPanel.add(checkOutDateField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int roomNumber = Integer.parseInt(roomNumberField.getText());
                        String customerName = customerNameField.getText();
                        String contactDetails = contactDetailsField.getText();
                        Customer customer = new Customer(customerName, contactDetails);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = dateFormat.parse(checkInDateField.getText());
                        Date endDate = dateFormat.parse(checkOutDateField.getText());

                        hotel.bookRoom(roomNumber, customer, startDate, endDate);
                        JOptionPane.showMessageDialog(null, "Room booked successfully!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input or date format!");
                    }
                }
            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for checking out
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Enter Room Number to check out:"));
                JTextField roomNumberField = new JTextField();
                inputPanel.add(roomNumberField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int roomNumber = Integer.parseInt(roomNumberField.getText());
                        hotel.checkOut(roomNumber);
                        JOptionPane.showMessageDialog(null, "Room checked out successfully!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                    }
                }
            }
        });
        
        viewBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Improved logic for viewing all booked rooms information
                ArrayList<Room> bookedRooms = new ArrayList<>();

                for (Room room : hotel.getRooms()) {
                    if (room.isOccupied() && room.getCustomer() != null) {
                        bookedRooms.add(room);
                    }
                }

                if (!bookedRooms.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String[][] data = new String[bookedRooms.size()][6];
                    int i = 0;

                    for (Room room : bookedRooms) {
                        Customer customer = room.getCustomer();

                        data[i][0] = String.valueOf(room.getRoomNumber());
                        data[i][1] = String.valueOf(!room.isOccupied());
                        data[i][2] = customer.getCheckInDate() != null ? dateFormat.format(customer.getCheckInDate()) : "Not available";
                        data[i][3] = customer.getCheckOutDate() != null ? dateFormat.format(customer.getCheckOutDate()) : "Not available";
                        data[i][4] = customer.getName();
                        data[i][5] = customer.getContactDetails();

                        i++;
                    }

                    String[] columnNames = {"Room Number", "Availability", "Check-in Date", "Check-out Date", "Customer Name", "Contact Details"};

                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);

                    JOptionPane.showMessageDialog(null, scrollPane, "Booked Rooms Information", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No rooms are currently booked.");
                }
            }
        });

        panel.add(text);
        panel.add(viewAllRoomsButton);
        panel.add(viewBookingButton);
        panel.add(addRoomButton);
        panel.add(removeRoomButton);
        panel.add(checkAvailabilityButton);
        panel.add(bookRoomButton);
        panel.add(checkOutButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}

public class GUI {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("My Hotel", "123 Main St", "123-456-7890");
        HotelManagementGUI hotelManagementGUI = new HotelManagementGUI(hotel);
    }
}

