package thinh.shop.computer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.entity.ProductVariant;
import thinh.shop.computer.entity.VariantAttribute;
import thinh.shop.computer.repository.ProductRepository;
import thinh.shop.computer.repository.ProductVariantRepository;
import thinh.shop.computer.service.BrandService;
import thinh.shop.computer.service.CategoryService;
import thinh.shop.computer.service.ProductService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminProductController {
    @Autowired
    public CategoryService categoryService;
    @Autowired
    public BrandService brandService;
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public ProductVariantRepository productVariantsRepository;
    @Autowired
    public ProductService productService;
    @GetMapping("/admin/products")
    public String manageProducts(Model model) {
        List<Product> products = productService.allProduct();
        model.addAttribute("products", products);
        return "admin/products";
    }
    @GetMapping("/admin/products/add")
    public String showAddProductForm(Model model) {
        Product product = new Product();

        product.setVariants(new java.util.ArrayList<>());
        product.getVariants().add(new ProductVariant());

        model.addAttribute("product", product);
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("brands", brandService.getAllBrand());

        return "admin/product-add";
    }
    @PostMapping("/admin/products/add")
    public String saveProduct(@ModelAttribute("product") Product product, Model model) {

        if (product.getVariants() != null) {
            product.getVariants().removeIf(v -> v == null || v.getSku() == null || v.getSku().trim().isEmpty());

            Set<String> skuSet = new HashSet<>();

            for (ProductVariant variant : product.getVariants()) {
                String currentSku = variant.getSku().trim();

                if (!skuSet.add(currentSku)) {
                    model.addAttribute("errorMessage", "Lỗi: Bạn đang nhập trùng Mã SKU (" + currentSku + ") ở 2 dòng cấu hình khác nhau!");
                    model.addAttribute("categories", categoryService.getAllCategory());
                    model.addAttribute("brands", brandService.getAllBrand());
                    return "admin/product-add";
                }


                if (productVariantsRepository.existsBySku(currentSku)) {
                    model.addAttribute("errorMessage", "Lỗi: Mã SKU (" + currentSku + ") đã tồn tại trên hệ thống. Vui lòng đổi mã khác!");
                    model.addAttribute("categories", categoryService.getAllCategory());
                    model.addAttribute("brands", brandService.getAllBrand());
                    return "admin/product-add";
                }

                variant.setProduct(product);

                if (variant.getAttributes() != null) {
                    variant.getAttributes().removeIf(attr ->
                            attr == null ||
                                    attr.getName() == null || attr.getName().trim().isEmpty() ||
                                    attr.getValue() == null || attr.getValue().trim().isEmpty()
                    );

                    for (VariantAttribute attribute : variant.getAttributes()) {
                        attribute.setVariant(variant);
                    }
                }
            }
        }

        productRepository.save(product);

        return "redirect:admin/products";
    }
    @PostMapping("/admin/products/delete")
    public String  deleteProduct(@RequestParam("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:admin/products";
    }
    @GetMapping("/admin/products/edit")
    public String editProduct(@RequestParam("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        model.addAttribute("product", product);
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("brands", brandService.getAllBrand());
        return "admin/product-edit";

    }
    @PostMapping("/admin/products/edit")
    public String updateProduct(@ModelAttribute("product") Product product, Model model) {

        if (product.getVariants() != null) {

            product.getVariants().removeIf(v -> v == null || v.getSku() == null || v.getSku().trim().isEmpty());

            for (ProductVariant variant : product.getVariants()) {
                variant.setProduct(product);

                if (variant.getAttributes() != null) {
                    variant.getAttributes().removeIf(attr ->
                            attr == null || attr.getName() == null || attr.getName().trim().isEmpty() ||
                                    attr.getValue() == null || attr.getValue().trim().isEmpty()
                    );

                    for (VariantAttribute attribute : variant.getAttributes()) {
                        attribute.setVariant(variant);
                    }
                }
            }
        }

        try {
            productRepository.saveAndFlush(product);
            return "redirect:admin/products";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi cập nhật: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("brands", brandService.getAllBrand());
            return "admin/product-edit";
        }
    }
}
