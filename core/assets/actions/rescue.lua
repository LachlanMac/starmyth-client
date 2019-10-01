type = "pickupeffect"
cooldown = 5
range = 100
life = 10
casttime = 0.1
interval = 10
toggle = true
togglemin = 1


function _ON_COOLDOWN(data)
  print("on Cooldown")
end

function _ON_CAST(data)

end

function _ON_MISS(data)
 
end

function _LOOP(data)
   data:getCaster():getStats():changeCurrentEnergy(-1)  
end

function _ON_HIT_ENTITY(data)
 
end

function _ON_TOGGLE_OFF(data)
  print("TOGGLE")
  data:getCaster():drop()
  data:getTarget():beDropped()
end

function _ON_HIT_TILE(data)

end

function _ON_END(data)
  
end
function _TEST()
 
end