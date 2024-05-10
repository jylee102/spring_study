package com.expenseTracker.service;


import com.expenseTracker.entity.Asset;
import com.expenseTracker.entity.Category;
import com.expenseTracker.repository.AssetRepository;
import jakarta.persistence.EntityNotFoundException;
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
        return assetRepository.findByMemberIdOrderByPosition(memberId);
    }

    // 카테고리 이름과 금액 변경
    public void updateAsset(Long id, String name, Double amount) throws Exception {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        asset.setName(name);
        asset.setAmount(amount);
    }

    // 카테고리 순서 변경
    public void updatePosition(Long id, int position) throws Exception {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        asset.setPosition(position);
    }

    // 카테고리 등록
    public Asset addAsset(Asset asset) throws Exception {
        return assetRepository.save(asset);
    }

    // 카테고리 삭제
    public void deleteAsset(Long id) throws Exception {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        assetRepository.delete(asset);
    }
}
