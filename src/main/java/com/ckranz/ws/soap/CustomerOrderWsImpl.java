package com.ckranz.ws.soap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cxf.feature.Features;

import com.bharath.ws.trainings.CreateOrdersRequest;
import com.bharath.ws.trainings.CreateOrdersResponse;
import com.bharath.ws.trainings.CustomerOrdersPortType;
import com.bharath.ws.trainings.DeleteOrdersRequest;
import com.bharath.ws.trainings.DeleteOrdersResponse;
import com.bharath.ws.trainings.GetOrdersRequest;
import com.bharath.ws.trainings.GetOrdersResponse;
import com.bharath.ws.trainings.Order;
import com.bharath.ws.trainings.Product;

@Features(features="org.apache.cxf.feature.LoggingFeature")
public class CustomerOrderWsImpl implements CustomerOrdersPortType {
	
	Map<BigInteger, List<Order>> customerOrders = new HashMap<>();
	int currentId;
	
	public CustomerOrderWsImpl() {
		init();
	}
	
	public void init() {
		List<Order> orders = new ArrayList<>();
		Order order = new Order();
		order.setId(BigInteger.valueOf(1));
		
		Product product = new Product();
		product.setId("1");
		product.setDescription("IPhone");
		product.setQuantity(BigInteger.valueOf(3));
		
		order.getProduct().add(product);
		orders.add(order);
		
		customerOrders.put(BigInteger.valueOf(++currentId), orders);
	}

	@Override
	public GetOrdersResponse getOrders(GetOrdersRequest request) {
		//get customer id as sent in the request
		BigInteger customerId = request.getCustomerId();
		//create obj "orders that contains a list of orders from HashMap for this customer id 
		List<Order> orders = customerOrders.get(customerId);
		
		//create new instance of response stub to answer the request
		GetOrdersResponse response = new GetOrdersResponse();
		//seltsam aber der einzige weg, da es keine set-methode gibt (siehe in Beschreibung in der Klasse)
		response.getOrder().addAll(orders);
		
		//return the list of all orders of customer with id customer id
		return response;
	}

	@Override
	public CreateOrdersResponse createOrders(CreateOrdersRequest request) {
		//get info from the request
		BigInteger customerId = request.getCustomerId();
		Order order = request.getOrder();
		
		//assume cumsomerid already has orders and add the new order to the others
		List<Order> orders = customerOrders.get(customerId);
		orders.add(order);
		
		//create new instance of response stub to answer the request with boolean
		CreateOrdersResponse response = new CreateOrdersResponse();
		response.setResult(true);
		
		return response;
	}

	@Override
	public DeleteOrdersResponse deleteOrders(DeleteOrdersRequest request) {
		BigInteger customerId = request.getCustomerId();
		BigInteger orderId = request.getOrderId();
		
		List<Order> orders = customerOrders.get(customerId);
		for (Iterator<Order> iter = orders.listIterator(); iter.hasNext(); ) {
			Order o = iter.next();
			if(o.getId() == orderId) {
				iter.remove();
			}
		}
		
		DeleteOrdersResponse response = new DeleteOrdersResponse();
		response.setResult(true);
		
		return response;
	}

}
