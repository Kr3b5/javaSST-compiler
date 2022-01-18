class ifwhileBoolTest {
    // IF - OK
    public void methIF() {
        if (m1 == 15) {
            return;
        } else {
            return;
        }
    }

    // IF - ERROR
    public void methIFError() {
        if (15) {
            return;
        } else {
            return;
        }
    }

    // WHILE - OK
    public void methWHILE() {
        while(m1 < m2) {
            m1 = m1 + 1;
        }
    }

    // WHILE - ERROR
    public void methWHILEError() {
        while(m1) {
            m1 = m1 + 1;
        }
    }
}