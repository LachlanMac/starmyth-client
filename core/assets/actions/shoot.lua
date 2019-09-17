type = "direct"
damage = 10
cooldown = 0.1
accuracy = 90
range = 700
speed = 400
life = 2


function _ON_CAST()
  val = target:getName()
  print("Casting on " .. val)
 
end

function _ON_MISS()
  print("MIss!")
end

function _ON_HIT()
  print("Hit!")
end

function _ON_END()
  print("End!")
end
