class Test2 {

    // TEST Return
    public void methVOID() {
        int m1;
        m1 = 10;
    }

    public int methRet() {
        int m1;
        m1 = 10;
        return;
    }

    public int methIfelse() {
        int m1;
        m1 = 15;

        if (m1 == 15) {
            return;
        } else {
            return;
        }
    }

    public int metherror() {
        int m1;
        m1 = 10;
    }

    // TEST IF WHILE Bool
    public void methIF() {
        int m1;
        m1 = 10;

        if (m1 == 15) {
            return;
        } else {
            return;
        }
    }

    public void methIFError() {
        int m1;
        m1 = 10;

        if (15) {
            return;
        } else {
            return;
        }
    }

    public void methWHILE() {
        int m1;
        m1 = 10;

        while(m1 < m2) {
            m1 = m1 + 1;
        }
    }

    public void methWHILEError() {
        int m1;
        m1 = 10;

        while(m1) {
            m1 = m1 + 1;
        }
    }
}