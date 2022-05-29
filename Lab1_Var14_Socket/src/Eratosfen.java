import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Eratosfen {
    private final Boolean[] primes;
    private int i;

    public Eratosfen(int n) {
        primes = new Boolean[n + 1];
        fillSieve();
    }

    private void fillSieve() {
        Arrays.fill(primes, true);
        primes[0] = false;
        primes[1] = false;
        for (int i = 2; i < primes.length; ++i) {
            if (primes[i]) {
                for (int j = 2; i * j < primes.length; ++j) {
                    primes[i * j] = false;
                }
            }
        }
    }

    public List<Integer> getPrimes() {
        i = 0;
        List<Integer> res = new LinkedList<>();
        Arrays.stream(primes).forEach(e -> {
            if (e) res.add(i);
            ++i;
        });
        return res;
    }
}