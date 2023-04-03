import org.example.app.StocksClient;
import org.example.app.StocksRequests;
import org.example.app.details.ShortStockInfo;
import org.junit.*;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.*;
public class IntegrationTest {
    public static StocksClient client;
    @ClassRule
    public static GenericContainer webServer = new FixedHostPortGenericContainer("stocks:1.0-SNAPSHOT")
            .withFixedExposedPort(8080,8080)
            .withExposedPorts(8080);

    @Before
    public void before() {
        StocksRequests.add("CA", 200, 123.45);
        StocksRequests.add("CB", 5000, 100.00);
        client = new StocksClient();
    }

    @After
    public void after() {
        StocksRequests.clear();
    }

    @Test
    public void successfulBuyingStocks() {
        long userId = client.addUser();
        long buyingAmount = 10;
        double userBalance = 1000000.0;
        client.addMoney(userId, userBalance);
        String company = "CA";
        double companyPrice = client.getPrice(company);

        assertTrue(client.buy(userId,company, buyingAmount, companyPrice));
        assertEquals(userBalance - buyingAmount * companyPrice, client.getFreeMoney(userId));

        ShortStockInfo expectedStock = new ShortStockInfo(company, buyingAmount);
        assertEquals(expectedStock, client.getShortStockInfo(userId, company));
    }

    @Test
    public void failBuyingStocksUpdatePrice() {
        long userId = client.addUser();
        long buyingAmount = 10;
        double userBalance = 1000.0;
        client.addMoney(userId, userBalance);
        String company = "CA";
        double priceOfCompany = client.getPrice(company);
        StocksRequests.update(company, 1000);

        assertFalse(client.buy(userId, company, buyingAmount, priceOfCompany));
        assertEquals(userBalance, client.getFreeMoney(userId));
        assertNull(client.getShortStockInfo(userId, company));
    }

    @Test
    public void failBuyingStocksNotEnoughMoney() {
        long userId = client.addUser();
        long buyingAmount = 10;
        double userBalance = 1000.0;
        client.addMoney(userId, userBalance);
        String company = "CA";
        double priceOfCompany = client.getPrice(company);

        assertFalse(client.buy(userId, company, buyingAmount, priceOfCompany));
        assertEquals(userBalance, client.getFreeMoney(userId));
        assertNull(client.getShortStockInfo(userId, company));
    }

    @Test
    public void failBuyingStocksNotEnoughStocksAmount() {
        long userId = client.addUser();
        long buyingAmount = 1000;
        double userBalance = 10000000.0;
        client.addMoney(userId, userBalance);
        String company = "CA";
        double priceOfCompany = client.getPrice(company);

        assertFalse(client.buy(userId, company, buyingAmount, priceOfCompany));
        assertEquals(userBalance, client.getFreeMoney(userId));
        assertNull(client.getShortStockInfo(userId, company));
    }

    @Test
    public void successfulSellingStocks() {
        long userId = client.addUser();
        long buyingAmount = 1000;
        double userBalance = 1000000.0;
        client.addMoney(userId, userBalance);
        String company = "CB";
        double companyPrice = client.getPrice(company);

        assertTrue(client.buy(userId,company, buyingAmount, companyPrice));
        assertTrue(client.sell(userId,company, buyingAmount/4, companyPrice));
        assertEquals(userBalance - buyingAmount * companyPrice * 3/4, client.getFreeMoney(userId));

        ShortStockInfo expectedStock = new ShortStockInfo(company, buyingAmount * 3/4);
        assertEquals(expectedStock, client.getShortStockInfo(userId, company));
    }

    @Test
    public void failSellingStocksNotEnoughStocks() {
        long userId = client.addUser();
        long buyingAmount = 1000;
        double userBalance = 1000000.0;
        client.addMoney(userId, userBalance);
        String company = "CB";
        double companyPrice = client.getPrice(company);

        assertTrue(client.buy(userId,company, buyingAmount, companyPrice));
        assertFalse(client.sell(userId,company, buyingAmount + 1, companyPrice));
        assertEquals(userBalance - buyingAmount * companyPrice, client.getFreeMoney(userId));

        ShortStockInfo expectedStock = new ShortStockInfo(company, buyingAmount);
        assertEquals(expectedStock, client.getShortStockInfo(userId, company));
    }
}
