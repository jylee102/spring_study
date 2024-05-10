package com.expenseTracker.controller;

import com.expenseTracker.constant.Type;
import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.entity.Member;
import com.expenseTracker.service.AssetService;
import com.expenseTracker.service.CategoryService;
import com.expenseTracker.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AssetController {

    private final MemberService memberService;
    private final AssetService assetService;

    @PostMapping("/asset/updateAsset")
    public @ResponseBody ResponseEntity updateName(@RequestBody Asset req) {

        try {
            assetService.updateAsset(req.getId(), req.getName(), req.getAmount());
            return new ResponseEntity<>(new String[]{req.getName(), Double.toString(req.getAmount()) + '원'}, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업데이트에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/asset/updatePosition")
    public @ResponseBody ResponseEntity updatePosition(@RequestBody Long[] order) {

        try {
            for (int i = 0; i < order.length; i++) {
                assetService.updatePosition(order[i], i+1);
            }

            return new ResponseEntity<>("성공적으로 변경되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("업데이트에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/asset/addAsset")
    public @ResponseBody ResponseEntity addAsset(@RequestBody Map<String, String> req, Principal principal) {
        try {
            Member member = memberService.getMember(principal.getName());

            int position = assetService.getAssets(member.getId()).size() + 1;

            Asset asset = new Asset(req.get("name"), member, Double.parseDouble(req.get("amount")), position);
            assetService.addAsset(asset);
            return new ResponseEntity<>("성공적으로 생성되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("생성에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/asset/deleteAsset/{id}")
    public @ResponseBody ResponseEntity deleteAsset(@PathVariable("id") Long id) {
        try {
            assetService.deleteAsset(id);
            return new ResponseEntity<>("성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}

