package group.orderplanning.controller


import group.itechart.orderplanning.controller.ClientController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification


@SpringBootTest
class ClientControllerSpec extends Specification {
    @Autowired
    private ClientController clientController;

    def "asset client controller creation"() {
        expect:
        clientController != null
    }
}
