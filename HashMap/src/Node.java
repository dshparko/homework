import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Node<K, V> {
    private final int hash;
    private final K key;
    private V value;

    Node(int hash, K key, V value) {
        this.hash = hash;
        this.key = key;
        this.value = value;
    }

}
