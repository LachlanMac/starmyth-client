type = "targetedbuff"
cooldown = 5
accuracy = 90
range = 700
life = 20
casttime = 0.1
interval = 5


function _ON_CAST(data)
  val = data:getTarget():getName()
  print("Casting on " .. val .. " is Poisoned!")
end

function _LOOP(data)
  data:getTarget():getStats():changeCurrentHP(-1)  
end