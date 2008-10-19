(import gsi.disasters.*)
;Con rete.add() busca un template con el mismo nombre de la clase
(defclass Disaster Disaster)
(defclass Resource Resource)
(defclass Assignment Assignment)

(deffacts states
  (state initial assign)
  (state assign free)
  (state free initial)
  (state initial)
)

(defrule next-state
   (declare (salience -1))
  ?s <-(state ?old)
   (state ?old ?new)
=>
   (retract ?s)
   (assert (state ?new))
)   


(defrule verfacts
    (state initial)
=>
(facts)
)

(defrule assignPolicemen
	(state assign)
    ?dis <- (Disaster   (type ?typeDisaster)(name ?nameDisaster)(policemen ?p )(id ?idDisaster)(traffic ?traffic))
    ?poli <- (Resource (type "police")(id ?idPolicemen)(name ?namePolicemen)(idAssigned 0))
    
    (test (or
        	(and(eq ?traffic "low")(< ?p 1))
            (and(eq ?traffic "medium")(< ?p 2)) 
            (and(eq ?traffic "high")(< ?p 3))  
        ))
    
   =>
    	(definstance Assignment (new Assignment (str-cat "Resource POLICE ** "  ?namePolicemen "(id:" ?idPolicemen")** assigned to --> " ?typeDisaster " - " ?nameDisaster "(id:"?idDisaster")")))
	   	(modify ?dis (policemen (+ ?p 1)))
    	(modify ?poli (idAssigned ?idDisaster))
        (call ReteDisasterDB associate ?idPolicemen ?idDisaster)
     )

(defrule assignAmbulance
	(state assign)
    ?dis <- (Disaster   (type ?typeDisaster)(name ?nameDisaster)(ambulances ?p )(id ?idDisaster)(slight ?slight)(serious ?serious)(trapped ?trapped))
    ?amb <- (Resource (type "ambulance")(id ?idAmbulance)(name ?nameAmbulance)(idAssigned 0))
    
    (test (or
            (and (or(> ?slight 0)(> ?serious 0)(> ?trapped 0))(< ?p 1)  )
        	(and (or(> ?slight 4)(> ?serious 2)(> ?trapped 4))(< ?p 2)  )
            (and (or(> ?slight 10)(> ?serious 4)(> ?trapped 10))(< ?p 3)  )
            (and (or(> ?slight 15)(> ?serious 8)(> ?trapped 15))(< ?p 4)  )
            (and (or(> ?slight 20)(> ?serious 16)(> ?trapped 20))(< ?p 5)  )
             
        ))
    
   =>
    	(definstance Assignment (new Assignment (str-cat "Resource AMBULANCE ** "  ?nameAmbulance "(id:" ?idAmbulance")** assigned to --> " ?typeDisaster " - " ?nameDisaster "(id:"?idDisaster")")))
	   	(modify ?dis (ambulances (+ ?p 1)))
    	(modify ?amb (idAssigned ?idDisaster))
        (call ReteDisasterDB associate ?idAmbulance ?idDisaster)
     )

(defrule assignFiremen
	(state assign)
    ?dis <- (Disaster   (type ?typeDisaster)(name ?nameDisaster)(firemen ?p )(id ?idDisaster)(size ?size)(state ?state)(slight ?slight)(serious ?serious)(trapped ?trapped))
    ?fir <- (Resource (type "firemen")(id ?idFiremen)(name ?nameFiremen)(idAssigned 0))
    
    (test (or
            (and (or(eq ?size "small")(> ?trapped 0))(< ?p 1)  )
            (and (or(eq ?size "medium")(> ?trapped 15) (and(eq ?size "small")(eq ?state "active")) )(< ?p 2)  )
        	(and (or(eq ?size "big")(> ?trapped 30)(and(eq ?size "medium")(eq ?state "active"))  )(< ?p 3)  )
            (and (or(eq ?size "huge")(> ?trapped 50)(and(eq ?size "big")(eq ?state "active"))  )(< ?p 4)  )
             
        ))
    
   =>
    	(definstance Assignment (new Assignment (str-cat "Resource FIREMEN   ** "  ?nameFiremen "(id:" ?idFiremen")** assigned to --> " ?typeDisaster " - " ?nameDisaster "(id:"?idDisaster")")))
	   	(modify ?dis (firemen (+ ?p 1)))
    	(modify ?fir (idAssigned ?idDisaster))
       	(call ReteDisasterDB associate ?idFiremen ?idDisaster)
     )


(defrule freePolicemen
	(state free)
   ?dis <- (Disaster (state "erased")(id ?idDisaster)(policemen ?p))
   ?pol <- (Resource (type "police")(idAssigned ?idAssigned)(name ?namePolicemen)(id ?idPolicemen))  
    
    (test (eq ?idDisaster ?idAssigned))
    =>
    
    (definstance Assignment (new Assignment (str-cat "Resource POLICE   ** "  ?namePolicemen "(id:" ?idPolicemen")** is Free now ")))
	   	(modify ?dis (policemen (- ?p 1)))
    	(modify ?pol (idAssigned 0))
    
    
   )


(defrule freeFiremen
	(state free)
   ?dis <- (Disaster (state "erased")(id ?idDisaster)(firemen ?p))
   ?fir <- (Resource (type "firemen")(idAssigned ?idAssigned)(name ?nameFiremen)(id ?idFiremen))  
    
    (test (eq ?idDisaster ?idAssigned))
    =>
    
    (definstance Assignment (new Assignment (str-cat "Resource FIREMEN   ** "  ?nameFiremen "(id:" ?idFiremen")** is Free now ")))
	   	(modify ?dis (firemen (- ?p 1)))
    	(modify ?fir (idAssigned 0))
    
    
   )


(defrule freeAmbulance
	(state free)
   ?dis <- (Disaster (state "erased")(id ?idDisaster)(ambulances ?p))
   ?amb <- (Resource (type "ambulance")(idAssigned ?idAssigned)(name ?nameAmbulance)(id ?idAmbulance))  
    
    (test (eq ?idDisaster ?idAssigned))
    =>
    
    (definstance Assignment (new Assignment (str-cat "Resource AMBULANCE   ** "  ?nameAmbulance "(id:" ?idAmbulance")** is Free now ")))
	   	(modify ?dis (ambulances (- ?p 1)))
    	(modify ?amb (idAssigned 0))
    
    
   )