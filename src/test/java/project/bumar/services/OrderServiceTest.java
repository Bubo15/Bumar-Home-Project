package project.bumar.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import project.bumar.BaseTest;
import project.bumar.constant.ErrorConstants;
import project.bumar.data.entities.Order;
import project.bumar.data.repositories.*;
import project.bumar.event.publiser.Publisher;
import project.bumar.exeption.EmptyKeyOrValueException;
import project.bumar.exeption.NotFoundException;
import project.bumar.exeption.base.BaseCustomException;
import project.bumar.services.models.order.OrderServiceModel;
import project.bumar.services.models.order.OrderUserDetailsServiceModel;
import project.bumar.services.services.*;
import project.bumar.services.services.impl.*;
import project.bumar.validate.Validator;
import project.bumar.validate.impl.ValidatorImpl;
import project.bumar.web.models.bindingModels.order.OrderCreateBindingModel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest extends BaseTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private NewsRepository newsRepository;

    private ProductService productService;

    private BusinessService businessService;

    private CategoryService categoryService;

    private SubcategoryService subcategoryService;

    private OrderService orderService;

    private Validator validator;

    private NewsService newsService;

    @Mock
    private FileUploaderService fileUploaderService;

    @Mock
    private Publisher publisher;


    @Before
    public void beforeEach(){
        ModelMapper modelMapper = new ModelMapper();

        newsService = new NewsServiceImpl(newsRepository ,fileUploaderService,publisher, modelMapper);
        businessService = new BusinessServiceImpl(modelMapper, businessRepository, fileUploaderService, publisher);
        validator = new ValidatorImpl(newsService ,businessService);
        categoryService = new CategoryServiceImpl(categoryRepository, modelMapper, publisher);
        subcategoryService = new SubcategoryServiceImpl(subcategoryRepository, categoryService, modelMapper, publisher);
        productService = new ProductServiceImpl(productRepository, categoryService, businessService, fileUploaderService, subcategoryService, publisher, modelMapper, validator);
        orderService = new OrderServiceImpl(orderRepository, productService, modelMapper, validator, publisher);
    }

    @Test
    public void getAllOrders_shouldReturnAllOrders(){
        orderRepository.saveAll(List.of(new Order(), new Order()));

        List<OrderServiceModel> orderServiceModels = orderService.getAllOrders();

        assertEquals(2, orderServiceModels.size());
    }

    @Test
    public void create_shouldCreateOrder_whenGiveValidData(){
        OrderCreateBindingModel orderCreateBindingModel = new OrderCreateBindingModel();
        orderCreateBindingModel.setUsername("Username");
        orderCreateBindingModel.setAddress("Address 15");
        orderCreateBindingModel.setCity("City");
        orderCreateBindingModel.setEmail("email@abv.bg");
        orderCreateBindingModel.setFirstName("FirstName");
        orderCreateBindingModel.setLastName("LastName");
        orderCreateBindingModel.setPhone("0000000000");
        orderCreateBindingModel.setPostCode("1000");
        orderCreateBindingModel.setTotalPrice("1000");
        orderCreateBindingModel.setProducts(new HashSet<>());

        Optional<OrderServiceModel> orderServiceModel = orderService.create(orderCreateBindingModel);

        assertEquals(orderCreateBindingModel.getUsername(), orderServiceModel.get().getUsername());
        assertEquals(orderCreateBindingModel.getAddress(), orderServiceModel.get().getAddress());
    }

    @Test
    public void create_shouldThrowException_whenGiveInValidData(){
        OrderCreateBindingModel orderCreateBindingModel = new OrderCreateBindingModel();
        orderCreateBindingModel.setProducts(Set.of(new HashMap<>(){{put("Product", 0); put("", 2);}}));

        BaseCustomException exception = assertThrows(EmptyKeyOrValueException.class, () -> {
            orderService.create(orderCreateBindingModel);
        });

        String expectedMessage = ErrorConstants.ORDER_PRODUCT_EMPTY_KEY_OR_VALUE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllOrdersByCurrentUser_shouldReturnAllOrders_whenGiveValidData(){
        Order order = new Order();
        order.setUsername("Username");
        order.setProducts(new HashMap<>());

        Order order1 = new Order();
        order1.setUsername("Username");
        order1.setProducts(new HashMap<>());

        orderRepository.saveAll(List.of(order1, order));

        List<OrderUserDetailsServiceModel> orderUserDetailsServiceModels = orderService.getAllOrdersByCurrentUser("Username");

        assertEquals(2, orderUserDetailsServiceModels.size());
    }

    @Test
    public void save_shouldSaveOrder(){
        orderService.save(new Order());
        assertEquals(1, orderRepository.count());
    }

    @Test
    public void deleteById_shouldDeleteOrder_whenGiveValidData(){
        Order order = new Order();
        order.setUsername("Username");
        order.setProducts(new HashMap<>());

        orderRepository.saveAndFlush(order);

        OrderUserDetailsServiceModel orderUserDetailsServiceModel = orderService.deleteById(order.getId());

        assertEquals(0, orderUserDetailsServiceModel.getCountOfProducts());
        assertEquals(0, orderRepository.count());
    }

    @Test
    public void deleteById_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            orderService.deleteById(-1);
        });

        String expectedMessage = ErrorConstants.ORDER_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getOrderById_shouldReturnOrder_whenGiveValidData(){
        Order order = new Order();
        order.setUsername("Username");
        order.setProducts(new HashMap<>());

        orderRepository.saveAndFlush(order);

        assertEquals(order.getUsername(), orderService.getOrderById(order.getId()).getUsername());
    }

    @Test
    public void getOrderById_shouldThrowException_whenGiveInValidData(){
        BaseCustomException exception = assertThrows(NotFoundException.class, () -> {
            orderService.getOrderById(-1);
        });

        String expectedMessage = ErrorConstants.ORDER_ID_NOT_FOUND;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
