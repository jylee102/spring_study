package com.expenseTracker.service;


import com.expenseTracker.entity.Asset;
import com.expenseTracker.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    // 로그인한 유저가 설정한 카테고리 불러오기
    public List<Asset> getAssets(Long memberId) {
        return assetRepository.findByMemberId(memberId);
    }
}
