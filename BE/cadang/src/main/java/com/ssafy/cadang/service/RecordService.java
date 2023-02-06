package com.ssafy.cadang.service;

import com.ssafy.cadang.domain.Drink;
import com.ssafy.cadang.domain.Order;
import com.ssafy.cadang.domain.OrderStatus;
import com.ssafy.cadang.domain.User;
import com.ssafy.cadang.dto.record.*;
import com.ssafy.cadang.error.CustomException;
import com.ssafy.cadang.error.ExceptionEnum;
import com.ssafy.cadang.repository.DrinkRepository;
import com.ssafy.cadang.repository.RecordReposiotry;
import com.ssafy.cadang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {
    private final RecordReposiotry recordReposiotry;
    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;
    private static OrderStatus[] recordStatus = {OrderStatus.RECORD, OrderStatus.PICKUP};

    @Value("${USER_PROFILE_PATH}")
    private String UserProfileImgPath;

    @Transactional
    public Long saveRecordDirectly(RecordSaveRequestDto recordDto) throws IOException {
        User user = userRepository.findById(recordDto.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));
        Drink drink = drinkRepository.findById(recordDto.getDrinkId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.DRINK_NOT_FOUND));
        LocalDateTime regDate = LocalDateTime.now();
        if (recordDto.getRegDate() != null) {
            regDate = LocalDate.parse(recordDto.getRegDate()).atStartOfDay();
        }
        // 파일 업로드
        String imgUrl = uploadImage(recordDto.getImage());
        if (imgUrl == null)
            imgUrl = recordDto.getImage_url();


        Order record = Order.builder()
                .user(user)
                .drink(drink)
                .regDate(regDate)
                .caffeine(recordDto.getCaffeine())
                .sugar(recordDto.getSugar())
                .cal(recordDto.getCal())
                .price(recordDto.getPrice())
                .shot(recordDto.getShot())
                .whip(recordDto.getWhip())
                .sugarContent(recordDto.getSugarContent())
                .syrup(recordDto.getSyrup())
                .vanilla(recordDto.getVanilla())
                .hazelnut(recordDto.getHazelnut())
                .caramel(recordDto.getCaramel())
                .photo(imgUrl)
                .storeName(recordDto.getStoreName())
                .orderStatus(OrderStatus.RECORD)
                .build();
        Order saveRecord = recordReposiotry.save(record);
        return saveRecord.getId();
    }


//    public MyPageRecordListDto getOrderBySlice(Long lastUpdateId, Long userId, int size) {
//        PageRequest pageRequest = PageRequest.of(0, size);
//        Order lastRecord = recordReposiotry.findById(lastUpdateId).orElseThrow(() -> new NoSuchElementException());
//
//        Slice<Order> orders = recordReposiotry.findByIdLessThanAndUserIdAndOrderStatusIn(lastRecord.getRegDate(), userId, recordStatus, pageRequest);
//        List<MyPageRecordDto> recordDtos = toMyPageRecordDtos(orders);
//        return MyPageRecordListDto.builder()
//                .recordList(recordDtos)
//                .hasNext(orders.hasNext())
//                .build();
//    }

    public RecordDetailDto getOrderByRecordId(Long recordId) {
        Optional<Order> order = recordReposiotry.findById(recordId);
        if (order.isEmpty())
            throw new CustomException(ExceptionEnum.RECORD_NOT_FOUND);
        return toRecordDetailDto(order.get());

    }

    @Transactional
    public Long deleteOrderById(Long recordId) {
        Optional<Order> order = recordReposiotry.findById(recordId);
        if (order.isEmpty())
            throw new CustomException(ExceptionEnum.RECORD_NOT_FOUND);
        recordReposiotry.delete(order.get());
        return recordId;
    }

    public MyPageRecordListDto searchByKeyword(Long userId, String keyword, int page, int size) {
        // pagination
        PageRequest pageRequest = PageRequest.of(page, size);
        Slice<Order> orders;
        if (keyword == null) // keyword가 null면 전체 조회
            orders = recordReposiotry.findAllByPage(userId, recordStatus, pageRequest);
        else {
            // 키워드 검색
            keyword = "%" + keyword + "%";
            orders = recordReposiotry.findBySearchKeyword(userId, keyword, recordStatus, pageRequest);
        }
        List<MyPageRecordDto> recordDtos = toMyPageRecordDtos(orders);
        return MyPageRecordListDto.builder()
                .recordList(recordDtos)
                .hasNext(orders.hasNext())
                .build();

    }

    @Transactional
    public Long updateRecord(RecordUpdateDto updateDto) throws IOException {
        Order findRecord = recordReposiotry.findById(updateDto.getId())
                .orElseThrow(() -> new CustomException(ExceptionEnum.RECORD_NOT_FOUND));
        if (findRecord.getOrderStatus() == OrderStatus.PICKUP && updateDto.getRegDate() != null) {
            throw new CustomException(ExceptionEnum.RECORD_NOT_ALLOWED_MODIFY);
        }
        if (updateDto.getRegDate() != null) {
            LocalDateTime localDateTime = LocalDate.parse(updateDto.getRegDate()).atStartOfDay();
            findRecord.setRegDate(localDateTime);
        }
        if (updateDto.getMemo() != null)
            findRecord.setMemo(updateDto.getMemo());
        if (updateDto.getIsPublic() != null)
            findRecord.setPublic(updateDto.getIsPublic());

        // 파일 업로드
        String imgUrl = uploadImage(updateDto.getImage());
        if (imgUrl != null) {
            findRecord.setPhoto(imgUrl);
        }

        return findRecord.getId();

    }


    public int getSum(Long userId, int month) {
        return recordReposiotry.findSumByUserAndMonth(userId, month);
    }

    private List<MyPageRecordDto> toMyPageRecordDtos(Slice<Order> orders) {
        return orders.getContent()
                .stream()
                .map(o -> MyPageRecordDto.builder()
                        .id(o.getId())
                        .storeName(o.getStoreName())
                        .drinkName(o.getDrink().getDrinkName())
                        .regDate(o.getRegDate())
                        .caffeine(o.getCaffeine())
                        .sugar(o.getSugar())
                        .cal(o.getCal())
                        .price(o.getPrice())
                        .isPublic(o.isPublic())
                        .memo(o.getMemo())
                        .photo(o.getPhoto())
                        .build())
                .sorted(Comparator.comparing(MyPageRecordDto::getRegDate).reversed())
                .collect(Collectors.toList());
    }

    private RecordDetailDto toRecordDetailDto(Order order) {
        return RecordDetailDto.builder()
                .id(order.getId())
                .photo(order.getPhoto())
                .drinkName(order.getDrink().getDrinkName())
                .isPublic(order.isPublic())
                .regDate(order.getRegDate())
                .memo(order.getMemo())
                .size(order.getDrink().getSize())
                .shot(order.getShot())
                .whip(order.getWhip())
                .sugarContent(order.getSugarContent())
                .syrup(order.getSyrup())
                .vanilla(order.getVanilla())
                .caramel(order.getCaramel())
                .hazelnut(order.getHazelnut())
                .orderStatus(order.getOrderStatus())
                .build();
    }


    private String uploadImage(MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            MultipartFile file = image;
            String uuid = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fullPath = UserProfileImgPath + uuid + "_" + originalFilename;
            file.transferTo(new File(fullPath));
            return fullPath;
        }
        return null;
    }


}
