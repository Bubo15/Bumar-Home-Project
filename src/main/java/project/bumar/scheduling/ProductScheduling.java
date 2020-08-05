package project.bumar.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.bumar.data.entities.base.BaseProduct;
import project.bumar.service.services.ProductService;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class ProductScheduling {

    private final ProductService productService;

    @Autowired
    public ProductScheduling(ProductService productService) {
        this.productService = productService;
    }

    // Every day will check every product whether, 7 days have passed since it's creation

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkWhetherProductHaveToBeOld() {
        this.productService
                .getAllNewProduct()
                .forEach(product -> {
                    BaseProduct baseProduct = this.productService.getProductById(product.getId());
                    LocalDateTime newProductDeadLine = baseProduct.getCreated().plusDays(7);
                    if ((newProductDeadLine.isBefore(LocalDateTime.now()))) {
                        baseProduct.setNew(false);
                        this.productService.saveProduct(baseProduct);
                    }
                });
    }

}
