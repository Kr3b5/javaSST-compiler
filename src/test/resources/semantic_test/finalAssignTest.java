class finalAssignTest {
    //final
    final int fvar = 5;

    //global
    int gvar;


    // OK
    public void okAssignGlobal() {
        gvar = 2;
    }

    // OK
    public void errorAssignFinal() {
        fvar = 1;
    }
}