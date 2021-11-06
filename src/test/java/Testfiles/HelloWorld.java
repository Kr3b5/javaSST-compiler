package Testfiles;

/* HelloWorld.java */
@SuppressWarnings("ALL")
public class HelloWorld
{
    public static void main(String[] args) {
        System.out.println("Hello World!");

        int x = 2 + 3 - 1;
        int y = 5 * 7;
        double z = 3 / 2;

        if( x <= y ){
            System.out.println("1");
        }
        if( z >= y ){
            System.out.println("2");
        }
        if( x != z ){
            System.out.println("3,5");
        }else if( x == y ){
            System.out.println("4");
        }
    }
}