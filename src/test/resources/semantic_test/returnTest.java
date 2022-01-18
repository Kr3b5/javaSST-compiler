class returnTest {
    // OK
    public int methRet() {
        return;
    }

    // IF-ELSE OK
    public int methIfelse() {
        int m1;
        if (m1 == 15) {
            return;
        } else {
            return;
        }
    }

    // ERROR CASE
    public int metherror() {
        int m1;
        m1 = 10;
    }
}