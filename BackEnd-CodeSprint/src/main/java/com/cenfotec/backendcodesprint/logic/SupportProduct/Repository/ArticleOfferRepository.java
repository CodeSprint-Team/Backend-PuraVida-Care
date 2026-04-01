package com.cenfotec.backendcodesprint.logic.SupportProduct.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ArticleOffer;
import com.cenfotec.backendcodesprint.logic.Model.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleOfferRepository extends JpaRepository<ArticleOffer, Long> {

    List<ArticleOffer> findBySupportProductPostId(Long postId);

    List<ArticleOffer> findByBuyerUserId(Long buyerUserId);

    List<ArticleOffer> findBySupportProductPostUserIdOrderByCreatedDesc(Long ownerUserId);

    List<ArticleOffer> findByBuyerUserIdOrderByCreatedDesc(Long buyerUserId);

    List<ArticleOffer> findBySupportProductPostIdOrderByCreatedDesc(Long postId);

    Optional<ArticleOffer> findByIdAndSupportProductPostUserId(Long offerId, Long ownerUserId);

    List<ArticleOffer> findBySupportProductPostIdAndOfferState(Long postId, OfferStatus offerState);
}