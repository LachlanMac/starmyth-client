
_schedule = {7,7,7,7,7,2,2,7,7,7,7,7,7,7,0,0,0,3,3,3,3,3,3,3,3,3,7,7,7,7,3,3,3,3,3,5,5,5,5,5,5,5,5,0,0,0,0,0,0,0}

function _WORK(data)
    
end

function _COMBAT(data)
    
    this = data:getNPC()
    
    this:setSpeed(120)
    
	closest = this:getClosestTarget()
	
	if this:lookAt(closest) and this:isInRange(closest, 300) then
		   this:clearPath()
           this:castDirectAction("shoot", closest)
	else
	   if this:hasPath() then
		this:moveOnPath()	   
        else
	      this:findPathTo(closest)
        end
	end
	
end

function _RECREATION(data)
  
end

function _SLEEP(data)

end

function _DOWNED(data)
 
end

function _ON_LOAD()
   print("Captain Profession Loaded")
end

function _WANDER(data)
   if data:getNPC():hasPath() then
      data:getNPC():moveOnPath()
   end	
end



