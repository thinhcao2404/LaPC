package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thinh.shop.computer.dto.request.AttributeRequest;
import thinh.shop.computer.dto.request.ProductCreateRequest;
import thinh.shop.computer.dto.request.ProductUpdateRequest;
import thinh.shop.computer.dto.request.VariantRequest;
import thinh.shop.computer.dto.response.ProductResponse;
import thinh.shop.computer.entity.*;
import thinh.shop.computer.mapper.ProductMapper;
import thinh.shop.computer.repository.BrandRepository;
import thinh.shop.computer.repository.CategoryRepository;
import thinh.shop.computer.repository.ProductRepository;
import thinh.shop.computer.repository.ProductVariantRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    public ProductRepository  productRepository;
    @Autowired
    public ProductMapper productMapper;
    @Autowired
    public CategoryRepository categoryRepository;
    @Autowired
    public BrandRepository brandRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;

    public ProductResponse getProductById(Long productId){
        Product product = productRepository.findById(productId).orElse(null);
        return productMapper.toResponse(product);
}

    public List<ProductResponse> searchProducts(String keyword){
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public long countProduct(){
        return productRepository.count();
    }

    public Product findById(Long productId){
       return  productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

    }

    @Transactional
    public void deleteById(Long productId) {
        try {
            productRepository.deleteById(productId);
        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa! Sản phẩm này đã phát sinh giao dịch hoặc đơn hàng.");
        }
    }

    public List<ProductResponse> findByCategory_Id(long categoryId){
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateProduct(ProductUpdateRequest request){
        Product existingProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy Danh mục sản phẩm!"));
        Brand brand = brandRepository.findById(request.getBrand())
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy Hãng sản xuất!"));

        if (request.getVariants() != null) {
            request.getVariants().removeIf(v -> v == null || v.getSku() == null || v.getSku().trim().isEmpty());
            Set<String> formSkuSet = new HashSet<>();

            for (VariantRequest vReq : request.getVariants()) {
                String currentSku = vReq.getSku().trim();
                if (!formSkuSet.add(currentSku)) {
                    throw new RuntimeException("Bạn đang nhập trùng Mã SKU (" + currentSku + ") ở 2 dòng cấu hình khác nhau!");
                }
                if (vReq.getId() != null) {
                    boolean isDuplicate = productVariantRepository.existsBySkuAndIdNot(currentSku, vReq.getId());
                    if (isDuplicate) {
                        throw new RuntimeException("Mã SKU (" + currentSku + ") đã bị trùng với sản phẩm khác!");
                    }
                } else {
                    if (productVariantRepository.existsBySku(currentSku)) {
                        throw new RuntimeException("Mã SKU (" + currentSku + ") đã tồn tại trên hệ thống!");
                    }
                }
            }
        }

        // Xóa cấu hình
        Set<Long> requestVariantIds = request.getVariants() != null
                ? request.getVariants().stream()
                .map(VariantRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                : new HashSet<>();

        for (ProductVariant v : existingProduct.getVariants()) {
            if (!requestVariantIds.contains(v.getId())) {
                v.setActive(false);
            } else {
                v.setActive(true);
            }
        }

        productRepository.saveAndFlush(existingProduct);

        productMapper.updateEntityFromRequest(request, existingProduct, category, brand);

        productRepository.save(existingProduct);
    }

    @Transactional
    public void createProduct(ProductCreateRequest request){
        if(request.getVariants() != null){
            request.getVariants().removeIf(v -> v == null || v.getSku() == null || v.getSku().trim().isEmpty());

            Set<String> skuSet = new HashSet<>();
            for(VariantRequest variantRequest : request.getVariants()){
                String currentSku = variantRequest.getSku().trim();
                if(!skuSet.add(currentSku)){
                    throw new RuntimeException("Bạn đang nhập trùng Mã SKU (" + currentSku + ") ở 2 dòng cấu hình khác nhau!");
                }
                if(productVariantRepository.existsBySku(currentSku)){
                    throw new RuntimeException("Mã SKU (" + currentSku + ") đã tồn tại trên hệ thống!");
                }
            }
        }
        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(()-> new RuntimeException("Lỗi: Không tìm thấy Danh mục!"));
        Brand brand = brandRepository.findById((request.getBrand()))
                .orElseThrow(()->new RuntimeException("Lỗi: Không tìm thấy Hãng sản xuất!"));

        Product newProduct = productMapper.toEntityFromCreateRequest(request,category,brand);

        productRepository.save(newProduct);
    }

    public List<ProductResponse> getAllProductsForAdmin() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}
