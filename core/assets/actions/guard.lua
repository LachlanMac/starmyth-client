type = "direct"
damage = 10
cooldown = 0.1
accuracy = 90
range = 700
speed = 400
life = 2
casttime = 0.1


function _ON_CAST(data)
 
end

function _ON_MISS(data)
  print("MIss!")
end

function _ON_HIT_ENTITY(data)
  val = data:getHit():getName()
  print("Hit " .. val)
end

function _ON_HIT_TILE(data)
  print("HIT TILE")
end

function _ON_END(data)
  print("End!")
end
function _TEST()
  print("THISIS A TEST")
end