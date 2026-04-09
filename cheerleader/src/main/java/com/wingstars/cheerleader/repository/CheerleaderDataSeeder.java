package com.wingstars.cheerleader.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingstars.core.dto.CheerleaderJsonDTO;
import com.wingstars.cheerleader.entity.Cheerleader;
import com.wingstars.cheerleader.repository.CheerleaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j // Dùng để in log ra màn hình console
public class CheerleaderDataSeeder implements CommandLineRunner {

    private final CheerleaderRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        // Chỉ chạy script nếu trong bảng chưa có dữ liệu (chống nhân bản khi restart server)
        if (repository.count() > 0) {
            log.info("Database đã có dữ liệu Cheerleaders. Bỏ qua script Seeding.");
            return;
        }

        log.info("Bắt đầu đọc file cheerleaders.json và nạp vào Database...");

        try {
            // Đọc file từ thư mục resources
            InputStream inputStream = new ClassPathResource("cheerleaders.json").getInputStream();
            List<CheerleaderJsonDTO> jsonList = objectMapper.readValue(inputStream, new TypeReference<>() {});

            int count = 0;
            for (CheerleaderJsonDTO dto : jsonList) {
                // Kiểm tra an toàn: nếu đã có tên này rồi thì bỏ qua
                if (!repository.existsByFullName(dto.getName())) {
                    Cheerleader entity = mapToEntity(dto);
                    repository.save(entity);
                    count++;
                }
            }
            log.info("✅ Hoàn tất! Đã thêm thành công {} Cheerleaders vào Database.", count);

        } catch (Exception e) {
            log.error("❌ Lỗi khi đọc hoặc lưu dữ liệu từ file JSON: ", e);
        }
    }

    // Hàm chuyển đổi DTO sang Entity an toàn
    private Cheerleader mapToEntity(CheerleaderJsonDTO json) {
        Cheerleader entity = new Cheerleader();
        
        entity.setFullName(json.getName());
        entity.setAvatarUrl(json.getAvatarUrl());
        entity.setJerseyNumber(json.getNumber());
        entity.setAboutCheerleader(json.getAbout());
        entity.setMessageToFans(json.getSay());

        if (json.getSocial() != null) {
            entity.setFacebookUrl(StringUtils.hasText(json.getSocial().getFacebook()) ? json.getSocial().getFacebook() : null);
            entity.setInstagramUrl(StringUtils.hasText(json.getSocial().getInstagram()) ? json.getSocial().getInstagram() : null);
        }

        if (json.getProfile() != null) {
            entity.setZodiacSign(json.getProfile().getSign());
            
            // Lọc chữ "型" ra khỏi Blood Type (vd: "O型" -> "O")
            if (StringUtils.hasText(json.getProfile().getBloodType())) {
                entity.setBloodType(json.getProfile().getBloodType().replace("型", ""));
            }

            // Xử lý Hobbies: Đổi \r\n thành dấu phẩy
            if (StringUtils.hasText(json.getProfile().getInterest())) {
                entity.setHobbies(json.getProfile().getInterest().replaceAll("\\r\\n|\\r|\\n", ", "));
            }

            // Xử lý Ngày sinh
            if (StringUtils.hasText(json.getProfile().getBirthdate())) {
                try {
                    entity.setBirthDate(LocalDate.parse(json.getProfile().getBirthdate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (Exception ignored) {}
            }

            // Xử lý An toàn Chiều cao / Cân nặng với kiểu Integer theo định dạng Entity
            entity.setHeightCm(parseIntegerSafe(json.getProfile().getHeight()));
            entity.setWeightKg(parseIntegerSafe(json.getProfile().getWeight()));
        }

        return entity;
    }

    private Integer parseIntegerSafe(String val) {
        if (!StringUtils.hasText(val)) return null;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return null; // Gặp chữ thì trả về null chứ không báo lỗi
        }
    }
}