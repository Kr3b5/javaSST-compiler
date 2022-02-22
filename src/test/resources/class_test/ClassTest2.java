class ClassTest2 {
    final int fvar1 = 1;
    final int fvar2 = 3;

    int dyn1;
    int dyn2;

    public void meth1() {
        int var2;
        int var3;

        var2 = 5;

        dyn1 = meth2(fvar1, var2);

        dyn2 = methif(fvar2);

        return;
    }

    public int meth2(int m, int n) {
        return m + n;
    }

    public int meth3(int m) {
        return 5 + m + m;
    }

    public int meth4(int m, int n) {
        return m * n + (m/n);
    }


    public int methif(int m) {
        if (m >= 9) {
            return m;
        } else {
            m = m - 6;
            return m;
        }
    }

    public int methwhile(int m) {
        int var4;

        var4 = 15;

        while(m <= 9){
            m = meth2(m,7);
            var4 = var4 + 10;
        }
        return var4;
    }
}