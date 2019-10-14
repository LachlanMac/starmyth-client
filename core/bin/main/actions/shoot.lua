type = "direct"
damage = 5
cost = 1
cooldown = 0.5
accuracy = 90
range = 700
speed = 400
life = 2
casttime = 0.1


function _ON_CAST(data)
 
end

function _ON_MISS(data)

end

function _ON_HIT_ENTITY(data)
  data:getEntityHit():getStats():changeCurrentHP(-damage)
 
end

function _ON_NOT_ENOUGH_ENERGY(data)
  print("Not enough energy")
end

function _ON_HIT_TILE(data)

end

function _ON_END(data)

end

function _LOOP(data)

end

function _TEST()

end
