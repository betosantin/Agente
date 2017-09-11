
package Entity;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class PinLayout {

    public static PinLayout PIBORG_LEDBORG = new PinLayout(RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_00);
    
    private final Pin redPin;
    private final Pin greenPin;
    private final Pin bluePin;

    public PinLayout(Pin redPin, Pin greenPin, Pin bluePin) {
        this.redPin = redPin;
        this.greenPin = greenPin;
        this.bluePin = bluePin;
    }

    public Pin getRedPin() {
        return redPin;
    }

    public Pin getGreenPin() {
        return greenPin;
    }

    public Pin getBluePin() {
        return bluePin;
    }
    
}
