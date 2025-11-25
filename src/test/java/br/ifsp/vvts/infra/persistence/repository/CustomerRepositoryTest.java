package br.ifsp.vvts.infra.persistence.repository;

import br.ifsp.vvts.infra.persistence.entity.customer.CPFEmbeddable;
import br.ifsp.vvts.infra.persistence.entity.customer.CustomerEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
@Tag("PersistenceTest")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository sut;

    private CustomerEntity customer;

    @BeforeEach
    void setUp() {
        CPFEmbeddable cpf = new CPFEmbeddable("11122233344");
        customer = new CustomerEntity(null, "Lucas", cpf);
        sut.save(customer);
    }

    @AfterEach
    void tearDown() {
        sut.deleteAll();
    }

    @Test
    @DisplayName("Should return customer by cpf id")
    void shouldReturnCustomerByCpf() {
        final String cpf = "11122233344";
        final Optional<CustomerEntity> result = sut.findByCpfNumber(cpf);

        assertThat(result).isPresent();
        assertThat(result.get().getCpf()).isEqualTo(customer.getCpf());
    }

    @Test
    @DisplayName("Should return null when not found customer by cpf id")
    void shouldReturnEmptyForNonExistentCpf() {
        final String cpf = "00001110000";
        final Optional<CustomerEntity> result = sut.findByCpfNumber(cpf);

        assertThat(result).isNotPresent();
    }
}
