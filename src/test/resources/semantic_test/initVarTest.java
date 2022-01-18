class initVarTest {
    //finals
    final int fvar = 1;

    //globals
    int gvar1;

    // OK - init in Method
    public void methOKinitinmethod() {
        int mvar1;
        mvar1 = 1;
    }

    // OK - init in class
    public void methOKinitasglobal() {
        gvar1 = fvar;
    }

    // ERROR - not init in class or method
    public void methError() {
        mvar1 = 1;
        gvar2 = 1;
    }
}