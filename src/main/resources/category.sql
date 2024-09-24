INSERT INTO `category` (`parent_category_id`, `category_name`)
VALUES (NULL, '맛집'),
       (NULL, '숙박'),
       (NULL, '액티비티');

INSERT INTO `category` (`parent_category_id`, `category_name`)
VALUES (1, '혼카페'),
       (1, '혼밥'),
       (1, '혼술');

INSERT INTO `category` (`parent_category_id`, `category_name`)
VALUES (2, '호텔/펜션'),
       (2, '게스트하우스'),
       (2, '모텔'),
       (2, '홈/빌라'),
       (2, '한옥');

INSERT INTO `category` (`parent_category_id`, `category_name`)
VALUES  (3, '수상레저'),
        (3, '관광지'),
        (3, '전시'),
        (3, '편집/소품샵');
