
_schedule = {7,7,7,7,7,2,2,7,7,7,7,7,7,7,0,0,0,3,3,3,3,3,3,3,3,3,7,7,7,7,3,3,3,3,3,5,5,5,5,5,5,5,5,0,0,0,0,0,0,0}

function _WORK(data)
    
end

function _COMBAT(data)
	closest = data:getNPC():getClosestTarget()
	if data:getNPC():lookAt(closest) then
           data:getNPC():castDirectAction("shoot", closest)
	else
	   if data:getNPC():hasPath() then
		print("moving on path")
		data:getNPC():moveOnPath()	   
           else
		print("finding new path")
	      data:getNPC():findPathTo(closest)
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
   data:getNPC():setSpin(false);
end



