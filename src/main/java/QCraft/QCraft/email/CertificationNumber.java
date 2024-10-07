package QCraft.QCraft.email;

public class CertificationNumber {

    public static String getCertificationNumber() {
        StringBuilder Number = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            Number.append((int) (Math.random() * 10));
        }

        return Number.toString();
    }
}
