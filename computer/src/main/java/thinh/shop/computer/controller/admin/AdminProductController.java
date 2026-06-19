package thinh.shop.computer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.dto.request.ProductCreateRequest;
import thinh.shop.computer.dto.request.ProductUpdateRequest;
import thinh.shop.computer.dto.request.VariantRequest;
import thinh.shop.computer.dto.response.ProductResponse;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.entity.ProductVariant;
import thinh.shop.computer.entity.VariantAttribute;
import thinh.shop.computer.mapper.ProductMapper;
import thinh.shop.computer.repository.ProductRepository;
import thinh.shop.computer.repository.ProductVariantRepository;
import thinh.shop.computer.service.BrandService;
import thinh.shop.computer.service.CategoryService;
import thinh.shop.computer.service.ProductService;
import thinh.shop.computer.service.ProductVariantService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminProductController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/admin/products")
    public String manageProducts(Model model) {
        List<ProductResponse> products = productService.getAllProductsForAdmin();
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/admin/products/add")
    public String showAddProductForm(Model model) {
        ProductUpdateRequest request =  new ProductUpdateRequest();

        request.getVariants().add(new VariantRequest());

        model.addAttribute("product", request);
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrand());

        return "admin/product-add";
    }

    @PostMapping("/admin/products/add")
    public String saveProduct(@ModelAttribute("product") ProductCreateRequest request, Model model) {
        try{
            productService.createProduct(request);
            return "redirect:/admin/products";
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories",categoryService.getAllCategories());
            model.addAttribute("brands",brandService.getAllBrand());
            return "admin/product-add";
        }
    }

    @PostMapping("/admin/products/delete")
    public String  deleteProduct(@RequestParam("id") Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/edit")
    public String editProduct(@RequestParam("id") Long id, Model model) {
        Product product = productService.findById(id);
        ProductUpdateRequest dto = productMapper.toUpdateRequestFromEntity(product);
        model.addAttribute("product", dto);
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrand());
        return "admin/product-edit";
    }

    @PostMapping("/admin/products/edit")
    public String updateProduct(@ModelAttribute("product") ProductUpdateRequest request, Model model) {
        try{
            productService.updateProduct(request);
            return "redirect:/admin/products";
        } catch (Exception e){
            model.addAttribute("errorMessage", "Lỗi khi cập nhật:"+e.getMessage());
            model.addAttribute("categories",categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrand());
            return  "admin/product-edit";
        }

    }
}
