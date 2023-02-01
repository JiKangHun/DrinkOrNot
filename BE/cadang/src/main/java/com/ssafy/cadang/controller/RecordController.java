package com.ssafy.cadang.controller;

import com.ssafy.cadang.dto.record.*;
import com.ssafy.cadang.service.RecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "기록", description = "기록 관련 api 입니다.")
@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public Long saveRecord(@RequestBody RecordSaveRequestDto recordDto) {
        Long id = recordService.saveOrderDirectly(recordDto);
        return id;
    }


    @GetMapping
    public MyPageRecordListDto searchByKeyword(@RequestParam Long userId,
                                               @RequestParam int page,
                                               @RequestParam int size,
                                               @RequestParam(required = false) String keyword) {

        return recordService.searchByKeyword(userId, keyword, page, size);
    }

    @GetMapping("/{recordId}")
    public RecordDetailDto recordByRecordId(@PathVariable Long recordId) {
        return recordService.getOrderByRecordId(recordId);
    }

    @PutMapping
    public Long updateRecord(@RequestBody RecordUpdateDto updateDto) {
        return recordService.updateRecord(updateDto);
    }

    @DeleteMapping("/{recordId}")
    public Long deleteByRecordId(@PathVariable Long recordId) {
        return recordService.deleteOrderById(recordId);
    }


}
