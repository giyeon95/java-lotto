package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void init() {
        wallet = new Wallet(10000);
    }

    @Test
    @DisplayName("Wallet 객체가 잘 생성되어야 한다.")
    void createTest() throws NoSuchFieldException, IllegalAccessException {
        int moneyByReflection = getMoneyByReflection(wallet);
        assertThat(moneyByReflection).isEqualTo(10000);
    }

    @Test
    @DisplayName("Wallet 객체가 Caching이 적용되어야 한다.")
    void walletCacheTest() {
        assertThat(Wallet.create(1000)).isSameAs(Wallet.create(1000));
    }

    @Test
    @DisplayName("Wallet 객체 생성시, 음수가 들어가면 RuntimeException이 발생한다.")
    void walletCreateExceptionTest() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Wallet(-1000));
    }

    @Test
    @DisplayName("금액을 사용한경우, 지갑에서 사용한 금액이 차감된다.")
    void spendTest() throws NoSuchFieldException, IllegalAccessException {

        Wallet afterSpendMoney = wallet.spend(2000);
        int moneyByReflection = getMoneyByReflection(afterSpendMoney);

        assertThat(moneyByReflection).isEqualTo(8000);
    }

    @Test
    @DisplayName("지출 금액이 음수인 경우, RuntimeException이 발생한다.")
    void spendExceptionTest() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> wallet.spend(-20000));
    }

    @Test
    @DisplayName("지출금액이 보유 금액보다 큰경우 RuntimeException이 발생한다.")
    void spendException2Test() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> wallet.spend(12000));
    }

    @Test
    @DisplayName("LottoTicket이 1000원 이라면, 구매가능한 Ticket이 갯수가 정상적으로 반환된다.")
    void getNumberOfBuyAvailableLottoTicketTest() {
        assertThat(wallet.getNumberOfBuyAvailable(1000)).isEqualTo(10);
    }

    private static int getMoneyByReflection(Wallet wallet)
        throws NoSuchFieldException, IllegalAccessException {
        Field walletField = Wallet.class.getDeclaredField("money");
        walletField.setAccessible(true);
        return (int) walletField.get(wallet);
    }
}