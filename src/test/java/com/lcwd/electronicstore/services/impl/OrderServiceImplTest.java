package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.*;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.repositories.CartRepository;
import com.lcwd.electronicstore.repositories.OrderRepository;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class OrderServiceImplTest {
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapper mapper;

    private User user;

    private Cart cart;

    private CartItem cartItem1;

    private CartItem cartItem2;

    private Product product;

    private Product product1;

    private Order order;

    private Order order1;

    @BeforeEach
    public void init(){

        user= User.builder().name("Ekta")
                .email("gedekarEkta123@gmail.com")
                .about("This is Testing method")
                .imagename("ekta.png")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

        product= Product.builder()
                .title("Nokia X2")
                .description("This is also old version")
                .price(10000)
                .discountedPrice(1000)
                .quantity(8)
                .live(true)
                .stock(true)
                .productImage("x2.png")
                .build();

        product1= Product.builder()
                .title("Apple phone")
                .description("This is new version")
                .price(10000)
                .discountedPrice(1000)
                .quantity(8)
                .live(true)
                .stock(true)
                .productImage("apple.png")
                .build();


        cartItem1 = CartItem.builder()
                .product(product)
                .totalPrice(56000)
                .quantity(10)
                .build();

        cartItem2= CartItem.builder()
                .product(product1)
                .totalPrice(12000)
                .quantity(8)
                .build();

        List list=new ArrayList(List.of(cartItem1, cartItem2));

        cart = Cart.builder()
                .items(list)
                .user(user)
                .build();
        cart.setCreatedBy("Ekta");
        cart.setLastModifiedBy("Ekta");
        cart.setIsActive("Yes");


        order= Order.builder()
                .billingAddress("Hanuman nagar, Pune")
                .billingName("Ekta Manoj Mankar")
                .billingPhone("9970306253")
                .deliveredDate(null)
                .paymentStatus("Not Paid")
                .orderStatus("Pending")
                .user(user)
                .build();

        OrderItems orderItems1=OrderItems.builder()
                .quantity(10)
                .product(product)
                .totalPrice(56000)
                .order(order)
                .build();

        OrderItems orderItems2=OrderItems.builder()
                .quantity(8)
                .product(product)
                .totalPrice(12000)
                .order(order)
                .build();

        order.setOrderItems(List.of(orderItems1, orderItems2));
        order.setOrderAmount(560000+8*12000);
        order.setCreatedBy("Ekta");
        order.setLastModifiedBy("Ekta");
        order.setIsActive("Yes");

         order1=Order.builder()
                .billingAddress("Pratap Nagar, Nagpur")
                .billingName("Miransh Manoj Mankar")
                .billingPhone("7972565299")
                .deliveredDate(null)
                .paymentStatus("Not Paid")
                .orderStatus("Pending")
                .user(user)
                .build();

        order.setOrderAmount(560000+8*12000);
        order.setCreatedBy("Miransh");
        order.setLastModifiedBy("Miransh");
        order.setIsActive("Yes");




    }
    @Test
    public void createOrderTest() {

        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .userId("ghj456")
                .cartId("ser345")
                .billingAddress("Hanuman nagar, Pune")
                .billingName("Ekta Manoj Mankar")
                .billingPhone("9970306253")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user)).thenThrow(new ResourceNotFoundException("User is not Found!!"));
        Mockito.when(cartRepository.findById(Mockito.anyString())).thenReturn(Optional.of(cart)).thenThrow(new ResourceNotFoundException("Cart of User is not found!!"));


        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order);

        OrderDto orderDto = orderService.createOrder(orderRequest);

        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
        Assertions.assertNotNull(order);
        Assertions.assertEquals("Ekta Manoj Mankar",orderDto.getBillingName());

    }

    @Test
    public void removeOrderTest() {

        String orderId="ghh677";

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.removeOrder(orderId);

        Mockito.verify(orderRepository, Mockito.times(1)).delete(order);
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.removeOrder("io90"));

    }

    @Test
    public void getOrdersOfUserTest() {

        String userId="hjj78";



        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(orderRepository.findByUser(Mockito.any())).thenReturn(List.of(order, order1));

        List<OrderDto> orderDtos = orderService.getOrdersOfUser(userId);

        Assertions.assertNotNull(orderDtos);
        Assertions.assertEquals(2, orderDtos.size());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.getOrdersOfUser("we34"));

    }

    @Test
    public void getOrdersTest() {

        List<Order> orderList = List.of(order, order1);
        PageImpl<Order> page = new PageImpl<>(orderList);

        Mockito.when(orderRepository.findAll((Pageable)(Mockito.any()))).thenReturn(page);

        PageableResponse<OrderDto> pageableResponse = orderService.getOrders(1, 2, "billingName", "desc");

        Assertions.assertNotNull(pageableResponse);
        Assertions.assertEquals(2, pageableResponse.getPageSize());
        Assertions.assertEquals(page.getTotalElements(), pageableResponse.getContent().size());
    }
    @Test
    public void updateOrderTest(){

        String orderId="tyy567";

        OrderDto orderDto = OrderDto.builder()
                .orderStatus("DELIVERED")
                .deliveredDate(new Date())
                .paymentStatus("PAID")
                .build();

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order);

        OrderDto updateOrder = orderService.updateOrder(orderId, orderDto);

        Assertions.assertNotNull(updateOrder);
        Assertions.assertEquals("PAID", updateOrder.getPaymentStatus());
        Assertions.assertEquals(order.getBillingName(), updateOrder.getBillingName());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> orderService.updateOrder("234w", this.mapper.map(order, OrderDto.class)));
    }
}