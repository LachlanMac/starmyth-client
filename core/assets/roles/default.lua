
function _RECREATION(data)
  
end

function _SLEEP(data)

end

function _DOWNED(data)
 
end

function _IDLE(data)

end

function _ON_LOAD()
   print("Captain Profession Loaded")
end


function _WANDER(data)
   print("CAPTAIN WANDER")
   if data:getNPC():hasPath() then
      data:getNPC():moveOnPath()
   end	
   data:getNPC():setSpin(false);
end