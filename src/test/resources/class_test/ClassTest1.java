class ClassTest1 {
    final int var1 = 0;

    int dyn1;

    public void meth1(int s) {
        int var2;
        int var3;

        var2 = 2;

        var3 = meth2(var1, var2);

        return;
    }

    public int meth2(int m, int n) {
        return m + n;
    }
}