package project.bumar.constant;

public final class EmailTemplateConstants {

    public final static String WELCOME_SUBJECT = "Welcome to Bumar Home!";

    public final static String ORDER_DELETE_TEMPLATE = "<h1>Order with id %d was deleted, because products are not available fot moment or you refused order.</h1>";

    public final static String ORDER_PRODUCT_DELETE_TEMPLATE = "<h1>Product %s was deleted, so will be deducted from total amount. We give our apologies and please your understanding. Our team wish you have nice and smile day!</h1>";

    public final static String PRODUCT_CREATE_TEMPLATE = "<h1>Good news. There is new product. For more information <a href=\"%s\">(link to product)</a>. You can visit our website on <a href=\"%s\">(link to website)</a>. Our team wish you have nice and smile day!</h1>";

    public final static String PRODUCT_DETAILS_LINK = "http://localhost:3000/product/details/%s";

    public static final Object WEBSITE_LINK = "http://localhost:3000";
}