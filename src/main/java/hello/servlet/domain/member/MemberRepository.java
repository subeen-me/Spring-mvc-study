package hello.servlet.domain.member;

import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository instance = new MemberRepository(); //싱글톤

    public static MemberRepository getInstance() {
        return instance;
    }

    // 싱글톤 쓸 때는 private 생성자로 아무나 쓸 수 없게 막아야 한다
    private MemberRepository() {

    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    // 스토어에 있는 values를 다 꺼내서 새로운 arrayList에 담는다.
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
