package AppKickstarter.misc;
//======================================================================
// Observer
public abstract class Observer {
    public abstract void update();
    protected Subject subject;

    //------------------------------------------------------------
    // Observer
    public Observer(Subject subject) {
        this.subject = subject;
    } // Observer
} // Observer
