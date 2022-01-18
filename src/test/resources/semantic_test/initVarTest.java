class ifwhileBoolTest {
    //globals
    int gvar1;

    // OK - init in Method
    public void methOKinitinmethod() {
        int mvar1;
        mvar1 = 1;
    }

    // OK - init in class
    public void methOKinitasglobal() {
        gvar1 = 1;
    }

    // ERROR - not init in class or method
    public void methError() {
        mvar1 = 1;
        gvar2 = 1;
    }
}