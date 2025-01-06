# User Interface Android Application - "The Lion King"

## **Overview**
This project is an Android application designed for a smooth and interactive movie ticket booking experience. The app enables users to select a movie theater, pick a date, choose ticket quantities, and review their order with animations and responsive UI components.

---

## **Key Features**

### 1. **Dynamic UI Interaction**
- Users can select a theater and specify the number of adult and child tickets using dropdown menus.
- A date picker ensures that users select valid show dates.

### 2. **Responsive Animations**
- Utilizes `AnimatorSet` for combined alpha and rotation effects to enhance UI feedback when all details (date, theater, and tickets) are valid, creating a smooth and satisfying confirmation animation.
- Uses `AnimationDrawable` to animate a sequence of images, adding visual appeal and engagement.

### 3. **Validation and Alerts**
- Input validation ensures required fields (date, theater, and tickets) are filled before proceeding.
- Error handling includes animated feedback (button shake) and error messages to guide users.

### 4. **Order Summary and Confirmation**
- Displays a real-time cost calculation based on ticket selections.
- Implements dialogs (`AlertDialog` for order and `BottomSheetDialog` for ticket details) to review and confirm orders.

### 5. **String Resource Localization**
- Supports English and Hebrew using string resource files.
- Automatically adapts text, messages, and labels based on the device's language settings, providing a seamless multilingual experience.

### 6. **Persistent State Management**
- Maintains the current selection of tickets, theater, and date until the purchase is completed or canceled.
