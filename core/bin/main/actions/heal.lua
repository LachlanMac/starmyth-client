type = "targetedbuff"
cooldown = 5
accuracy = 90
range = 700
life = 20
casttime = 0.1
interval = 5


function _ON_COOLDOWN(data)
  print("on Cooldown")
end

function _ON_CAST(data)
  val = data:getTarget():getName()
  print("Casting on " .. val)
end

function _ON_MISS(data)
  print("MIss!")
end

function _LOOP(data)
  data:getTarget():getStats():changeCurrentHP(15)
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
