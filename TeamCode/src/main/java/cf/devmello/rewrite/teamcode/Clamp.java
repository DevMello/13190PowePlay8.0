package cf.devmello.rewrite.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class Clamp {
    /*
     * The Clamp Swervo
     * @return Nothing
     * @code clamp.setPosition(x);
     */
    protected Servo clamp;
    /*
     * The Clamp State
     * Toggles between true or false
     * True = Clamp is closed
     * False = Clamp is open
     */
    protected boolean clampState = false;

    //initializing the object for the Class
    protected Clamp(Servo clamp) {
        this.clamp = clamp;
    }

    //method to set the clamp
    public void setClamp(Servo clamp) {
        this.clamp = clamp;
    }

    /**
     * Method that controls the clamp's state
     *
     * @<code>clamp()</code>
     */
    public void setClampState() {
        if (clampState) {
            //open clamp
            clamp.setPosition(0);
        } else {
            clamp.setPosition(1);
        }
    }
}
