SELECT     sum(s5.debt),
	   sum(CASE  WHEN s5.period > 0 AND   s5.period < 10 THEN s5.debt ELSE 0 END),
	   sum(CASE  WHEN s5.period >= 10 AND s5.period <= 20 THEN s5.debt ELSE 0 END), 
	   sum(CASE  WHEN s5.period > 20 AND s5.period <= 30 THEN  s5.debt ELSE 0 END), 
	   sum(CASE  WHEN s5.period > 30 THEN s5.debt  ELSE 0 END) 
FROM
	(SELECT s1.id_, (s1.sum_ - s2.sum_) debt,  (s1.sum_ - s2.sum_)/12*s4.VALUE*s3.sumArea period 
	FROM 
		(SELECT bfd.bp_id id_, SUM( bfd.SUMM ) sum_
		FROM bp_finance_documents bfd
		WHERE bfd.TYPE =  'BORROWING'  
			AND bfd.DATE_CONDUCTION >= TO_DATE('01.2000','MM.YYYY') 
			AND bfd.DATE_CONDUCTION <= &dateParam - 1     
		GROUP BY bfd.bp_id) s1, 
	
		(SELECT bfd.bp_id id_, SUM( bfd.SUMM ) sum_
		FROM bp_finance_documents bfd 
		WHERE bfd.TYPE =  'RETURN_BORROWING'
			AND bfd.DATE_CONDUCTION >= TO_DATE('01.2000','MM.YYYY') 
			AND bfd.DATE_CONDUCTION <= &dateParam - 1   
		GROUP BY bfd.bp_id) s2,
	
		(SELECT area.BP_ID id_, SUM( area.AREA ) sumArea
		FROM area
		GROUP BY id_) s3,
	
		(SELECT tariff.BP_ID id_, tariff.VALUE
		FROM tariff) s4

	WHERE s1.id_ = s2.id_
		AND s1.id_ = s3.id_
		AND s4.id_ = s1.id_) s5