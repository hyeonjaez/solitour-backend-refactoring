package solitour_backend.solitour.gathering.repository;

import com.querydsl.core.types.Projections;

import java.util.List;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import solitour_backend.solitour.book_mark_gathering.entity.QBookMarkGathering;
import solitour_backend.solitour.gathering.dto.response.GatheringBriefResponse;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.gathering.entity.QGathering;
import solitour_backend.solitour.gathering_applicants.entity.GatheringStatus;
import solitour_backend.solitour.gathering_applicants.entity.QGatheringApplicants;
import solitour_backend.solitour.gathering_category.entity.QGatheringCategory;
import solitour_backend.solitour.great_gathering.entity.QGreatGathering;
import solitour_backend.solitour.zone_category.entity.QZoneCategory;

public class GatheringRepositoryImpl extends QuerydslRepositorySupport implements GatheringRepositoryCustom {
    public GatheringRepositoryImpl() {
        super(Gathering.class);
    }

    QGathering gathering = QGathering.gathering;
    QZoneCategory zoneCategoryChild = QZoneCategory.zoneCategory;
    QZoneCategory zoneCategoryParent = new QZoneCategory("zoneCategoryParent");
    QBookMarkGathering bookMarkGathering = QBookMarkGathering.bookMarkGathering;
    QGreatGathering greatGathering = QGreatGathering.greatGathering;
    QGatheringCategory category = QGatheringCategory.gatheringCategory;
    QGatheringApplicants gatheringApplicants = QGatheringApplicants.gatheringApplicants;


    @Override
    public List<GatheringBriefResponse> getGatheringRecommend(Long gatheringId, Long gatheringCategoryId, Long userId) {
        QGreatGathering greatGatheringSub = new QGreatGathering("greatGatheringSub");

        return from(gathering)
                .join(zoneCategoryChild).on(zoneCategoryChild.id.eq(gathering.zoneCategory.id))
                .leftJoin(zoneCategoryParent).on(zoneCategoryParent.id.eq(zoneCategoryChild.parentZoneCategory.id))
                .leftJoin(bookMarkGathering)
                .on(bookMarkGathering.gathering.id.eq(gathering.id).and(bookMarkGathering.user.id.eq(userId)))
                .leftJoin(greatGathering).on(greatGathering.gathering.id.eq(gathering.id))
                .leftJoin(category).on(category.id.eq(gathering.gatheringCategory.id))
                .leftJoin(gatheringApplicants).on(gatheringApplicants.gathering.id.eq(gathering.id))
                .where(gathering.isFinish.eq(Boolean.FALSE)
                        .and(gathering.gatheringCategory.id.eq(gatheringCategoryId))
                        .and(gathering.id.ne(gatheringId))
                        .and(gatheringApplicants.gatheringStatus.eq(GatheringStatus.CONSENT)
                                .or(gatheringApplicants.gatheringStatus.isNull()))
                )
                .groupBy(gathering.id, zoneCategoryChild.id, zoneCategoryParent.id, category.id,
                        gathering.title, gathering.viewCount, gathering.user.name,
                        gathering.scheduleStartDate, gathering.scheduleEndDate,
                        gathering.deadline, gathering.allowedSex,
                        gathering.startAge, gathering.endAge, gathering.personCount)
                .orderBy(gathering.createdAt.desc())
                .select(Projections.constructor(
                        GatheringBriefResponse.class,
                        gathering.id,
                        gathering.title,
                        zoneCategoryParent.name,
                        zoneCategoryChild.name,
                        gathering.viewCount,
                        bookMarkGathering.user.id.isNotNull(),
                        greatGathering.gathering.count().coalesce(0L).intValue(),
                        category.name,
                        gathering.user.name,
                        gathering.scheduleStartDate,
                        gathering.scheduleEndDate,
                        gathering.deadline,
                        gathering.allowedSex,
                        gathering.startAge,
                        gathering.endAge,
                        gathering.personCount,
                        gatheringApplicants.count().coalesce(0L).intValue(),
                        new CaseBuilder()
                                .when(JPAExpressions.selectOne()
                                        .from(greatGatheringSub)
                                        .where(greatGatheringSub.gathering.id.eq(gathering.id)
                                                .and(greatGatheringSub.user.id.eq(userId)))
                                        .exists())
                                .then(true)
                                .otherwise(false)
                )).limit(3L).fetch();
    }


}
