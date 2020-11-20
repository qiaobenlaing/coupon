//
//  BMSQ_joinActivityViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/30.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_joinActivityViewController.h"

#import "SVProgressHUD.h"
#import "MobClick.h"
#import "BMSQ_PayCardViewController.h"
#import "BMSQ_actBuyCell.h"

@interface BMSQ_joinActivityViewController ()<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,BMSQ_actBuyCellDelegate>


@property (nonatomic, strong)UITableView *myTableView;

@property (nonatomic, strong)NSString *txtContent;//活动介绍


@property (nonatomic, strong)NSString *platBonus;//惠圈红包
@property (nonatomic, strong)NSString *shopBonus;//店家红包

@property (nonatomic, strong)NSString *useplatBonus;//惠圈红包
@property (nonatomic, strong)NSString *useshopBonus;//店家红包

@property (nonatomic, assign)float allUseBonus;//共用红包金额
@property (nonatomic, assign)float countMoney;//花费



@property (nonatomic, strong)NSMutableArray *feeScaleS;//支付类型数组
@property (nonatomic, strong)NSMutableArray *countS;//支付数量数组


@property (nonatomic, strong)NSString *bookingName;
@property (nonatomic, strong)NSString *mobileNbr;
@property (nonatomic, strong)NSString *orderAmount;//最终支付金额

@property (nonatomic, strong)NSString *shopCode;



@end

@implementation BMSQ_joinActivityViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"joinActivity"];// #import "MobClick.h"
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"joinActivity"];
}


-(void)keyboardWillShow{
    
    [UIView animateWithDuration:1 animations:^{
//        self.myTableView.frame = CGRectMake(0,APP_VIEW_ORIGIN_Y-(self.feeScaleS.count*35), APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y) ;
        
        self.myTableView.contentOffset = CGPointMake(0, (self.feeScaleS.count*20));
    }];
    
}
-(void)keyboardWillHide{
    [UIView animateWithDuration:1 animations:^{
//        self.myTableView.frame =CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y) ;
          self.myTableView.contentOffset = CGPointMake(0, 0);
    }];
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"活动报名"];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShow)
                                                 name:UIKeyboardWillShowNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillHide)
                                                 name:UIKeyboardWillHideNotification
                                               object:nil];
    

    
    
    self.feeScaleS = [[NSMutableArray alloc]init];
    self.countS = [[NSMutableArray alloc]init];
    
    self.platBonus = @"0";
    self.shopBonus = @"0";
    self.useshopBonus = @"0";
    self.useplatBonus = @"0";
    self.allUseBonus = 0;
    self.countMoney = 0;
    
    self.myTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.myTableView.delegate = self;
    self.myTableView.dataSource = self;
    self.myTableView.backgroundColor = [UIColor clearColor];
    self.myTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view insertSubview:self.myTableView belowSubview:self.navigationView];
    
    
    
    [self getInfoPreActInfo];
}



-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    if (section ==1) {
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 30)];
        label.backgroundColor = [UIColor whiteColor];
        label.text = @" 订单详情";
        label.font = [UIFont systemFontOfSize:13];
        
        return label;
    }else{
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 0)];
        label.backgroundColor = [UIColor whiteColor];
        label.text = @"订单详情";
        
        return label;
    }
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section ==1) {
        return 30;
    }else{
        return 0;
    }
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 7;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section ==6) {  //确定按扭
        return 1;
    }else if(section ==5){// 商家红包
        if([self.shopBonus floatValue]>0){
            return 1;
        }else{
            return 0;
        }
        
    }else if(section ==4){ // 惠圈红包
        if([self.platBonus floatValue]>0){
            return 1;
        }else{
            return 0;
        }
    }else if(section ==3){
        return 1;
    }else if(section ==2){
        return 1;
    }else if (section ==1){
        return self.feeScaleS.count;

    }else if (section ==0){
        return 1;
        
    }

  
    return 1;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==6) {  //确定按扭
        return 80;
    }
    else if(indexPath.section ==5){//// 商家红包
        return 40;
    }
    else if(indexPath.section ==4){//惠圈红包
        return 40;
        
    }else if(indexPath.section ==3){//联系人信息
        return 70;
        
    }else if(indexPath.section ==2){//订单总价
        return 42;
        
    }else if(indexPath.section ==1){//买
        return 55;
        
    }else if(indexPath.section ==0){
        
        CGSize size = [self.txtContent boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                                           options:NSStringDrawingUsesLineFragmentOrigin
                                                        attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13]}
                                                           context:nil].size;
        return size.height+22;
        
        
    }
    return 30;
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section == 6) {
        static  NSString *identifier = @"actCell5";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            UIButton *btn = [[UIButton alloc]initWithFrame:CGRectMake(20, 10, APP_VIEW_WIDTH-40, 40)];
            [btn setTitle:@"使用惠支付" forState:UIControlStateNormal];
            [btn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            btn.backgroundColor = APP_NAVCOLOR;
            btn.layer.cornerRadius = 5;
            btn.layer.masksToBounds = YES;
            btn.titleLabel.font = [UIFont systemFontOfSize:15.f];
            [cell addSubview:btn];
            
            [btn addTarget:self action:@selector(buyAct) forControlEvents:UIControlEventTouchUpInside];
        }
        return cell;
        
    }
    
    else  if (indexPath.section == 5) {// 商家红包
        static  NSString *identifier = @"actCell5";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *labelView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 39)];
            labelView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:labelView];
           
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 70, 39)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:13];
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
            [labelView addSubview:leftLabel];
            leftLabel.text = @"商家红包";
            
            UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-190, 0, 180, 39)];
            rightLabel.backgroundColor = [UIColor whiteColor];
            rightLabel.font = [UIFont systemFontOfSize:13];
            rightLabel.textColor = [UIColor blackColor];
            rightLabel.textAlignment = NSTextAlignmentRight;
            rightLabel.layer.borderColor = [[UIColor grayColor]CGColor];
            [labelView addSubview:rightLabel];
            rightLabel.tag = 100;
            
            UITextField *inputText = [[UITextField alloc]initWithFrame:CGRectMake(leftLabel.frame.origin.x+leftLabel.frame.size.width, 0, rightLabel.frame.origin.x-(leftLabel.frame.origin.x+leftLabel.frame.size.width), 39)];
            inputText.backgroundColor = [UIColor clearColor];
            inputText.textAlignment = NSTextAlignmentCenter;
            inputText.font = [UIFont systemFontOfSize:12];
            inputText.tag = 60000;
            inputText.placeholder = @"输入金额";
            inputText.keyboardType = UIKeyboardTypeDecimalPad;
            inputText.textColor = UICOLOR(182, 0, 12, 1.0);
            inputText.returnKeyType = UIReturnKeyDone;
            inputText.delegate = self;
            [inputText addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
            [labelView addSubview:inputText];
            
            
        }
        
        UILabel *rightLabel = [cell viewWithTag:100];
        rightLabel.text = [NSString stringWithFormat:@"元,商家红包可用%@元",self.shopBonus];
        
        return cell;
        
    }
    else  if (indexPath.section == 4) {//惠圈红包
        static  NSString *identifier = @"actCell4";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *labelView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 39)];
            labelView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:labelView];
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 70, 39)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:13];
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
            [labelView addSubview:leftLabel];
            leftLabel.text = @"惠圈红包";
            
            UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-190, 0, 180, 39)];
            rightLabel.backgroundColor = [UIColor whiteColor];
            rightLabel.font = [UIFont systemFontOfSize:13];
            rightLabel.textColor = [UIColor blackColor];
            rightLabel.textAlignment = NSTextAlignmentRight;
            rightLabel.layer.borderColor = [[UIColor grayColor]CGColor];
            [labelView addSubview:rightLabel];
            rightLabel.tag = 100;

            
            UITextField *inputText = [[UITextField alloc]initWithFrame:CGRectMake(leftLabel.frame.origin.x+leftLabel.frame.size.width, 0, rightLabel.frame.origin.x-(leftLabel.frame.origin.x+leftLabel.frame.size.width), 39)];
            inputText.backgroundColor = [UIColor clearColor];
            inputText.textAlignment = NSTextAlignmentCenter;
            inputText.font = [UIFont systemFontOfSize:12];
            inputText.tag = 50000;
            inputText.placeholder = @"输入金额";
            inputText.keyboardType = UIKeyboardTypeDecimalPad;
            inputText.textColor = UICOLOR(182, 0, 12, 1.0);
            inputText.returnKeyType = UIReturnKeyDone;
            inputText.delegate = self;
            [inputText addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
            [labelView addSubview:inputText];
           
            
        }
        
        
        UILabel *rightLabel = [cell viewWithTag:100];
        rightLabel.text = [NSString stringWithFormat:@"元,惠圈红包可用%@元",self.platBonus] ;

        return cell;
        
    }
    else if (indexPath.section ==3){ //联系人
        static  NSString *identifier = @"actCell3";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *labelView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 68)];
            labelView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:labelView];
            
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10,0, 70, 30)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:13];
            leftLabel.textColor = [UIColor blackColor];
            leftLabel.layer.borderColor = [[UIColor grayColor]CGColor];
            [labelView addSubview:leftLabel];
            leftLabel.text = @"联系人信息";
            
            UITextField *inputText = [[UITextField alloc]initWithFrame:CGRectMake(10,30,APP_VIEW_WIDTH/2-20, 30)];
            inputText.backgroundColor = [UIColor clearColor];
            inputText.font = [UIFont systemFontOfSize:12];
            inputText.tag = 100;
            inputText.placeholder = @" 预定人姓名";
            inputText.layer.borderColor = [APP_NAVCOLOR CGColor];
            inputText.layer.borderWidth = 0.3;
            inputText.layer.cornerRadius = 4;
            inputText.layer.masksToBounds = YES;
            inputText.returnKeyType = UIReturnKeyDone;
            inputText.delegate = self;
            [inputText addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
            [labelView addSubview:inputText];
            
            
            UITextField *mubText = [[UITextField alloc]initWithFrame:CGRectMake(inputText.frame.origin.x+inputText.frame.size.width+10, inputText.frame.origin.y,APP_VIEW_WIDTH/2-20, 30)];
            mubText.backgroundColor = [UIColor clearColor];
            mubText.font = [UIFont systemFontOfSize:12];
            mubText.tag = 200;
            mubText.placeholder = @" 有效手机号";
            mubText.keyboardType = UIKeyboardTypeDecimalPad;
            mubText.layer.borderColor = [APP_NAVCOLOR CGColor];
            mubText.layer.borderWidth = 0.3;
            mubText.layer.cornerRadius = 4;
            mubText.layer.masksToBounds = YES;
            mubText.returnKeyType = UIReturnKeyDone;
             mubText.delegate = self;
            [mubText addTarget:self action:@selector(chnageTextFild:) forControlEvents:UIControlEventEditingChanged];
            [labelView addSubview:mubText];
        }
        return cell;
    }else if(indexPath.section ==2){
        static NSString *identifier = @"payCell6";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 40)];
            bgView.backgroundColor = [UIColor whiteColor];
            [cell addSubview:bgView];
            
            
            
            UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, 60, 30)];
            leftLabel.backgroundColor = [UIColor whiteColor];
            leftLabel.font = [UIFont systemFontOfSize:13];
            leftLabel.text =@"订单价格";
            leftLabel.textColor = [UIColor blackColor];
            [bgView addSubview:leftLabel];
            
            
            UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, 5, 30, 30)];
            rightLabel.backgroundColor = [UIColor whiteColor];
            rightLabel.font = [UIFont systemFontOfSize:13];
            rightLabel.textAlignment = NSTextAlignmentCenter;
            rightLabel.text = @"元";
            rightLabel.textColor = UICOLOR(182, 0, 12, 1.0);
            
            [bgView addSubview:rightLabel];
            
            
            UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 5, APP_VIEW_WIDTH-80-30, 30)];
            countLabel.textAlignment = NSTextAlignmentRight;
            countLabel.backgroundColor = [UIColor clearColor];
            countLabel.font = [UIFont systemFontOfSize:13];
            countLabel.text = @"0";
            countLabel.textColor = UICOLOR(182, 0, 12, 1.0);
            countLabel.tag = 100;
            [bgView addSubview:countLabel];
            
        }
        
        UILabel *countLabel = (UILabel *)[cell viewWithTag:100];
        countLabel.text = self.orderAmount.length>0?self.orderAmount:@"0";
        
        return cell;

    }else if(indexPath.section ==1){   //买
        static NSString *identifier = @"actbuy1";
        BMSQ_actBuyCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[BMSQ_actBuyCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.buyDelegate = self;
        }
        
        [cell setCell:[self.feeScaleS objectAtIndex:indexPath.row] count:[self.countS objectAtIndex:indexPath.row] indexRow:(int)indexPath.row+1];
        
        return cell;
        
        
        
        
        
    }
    else if(indexPath.section ==0){   //标题
        static NSString *identifier = @"actbuy0";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UILabel *label = [[UILabel alloc]init];
            label.backgroundColor = [UIColor whiteColor];
            label.font = [UIFont systemFontOfSize:13];
            label.tag = 100;
            [cell addSubview:label];
            
        }
        
        CGSize size = [self.txtContent boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                                    options:NSStringDrawingUsesLineFragmentOrigin
                                                 attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13]}
                                                    context:nil].size;

        UILabel *label =(UILabel *) [cell viewWithTag:100];
        label.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, size.height+20);
        label.text = self.txtContent;
        return cell;
        
    }

    
    static  NSString *identifier = @"121";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];

    }
    return cell;
    
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:5];
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:index];
    UITextField *textField = (UITextField *)[cell viewWithTag:60000];
    [textField resignFirstResponder];
    
    index = [NSIndexPath indexPathForRow:0 inSection:4];
    cell = [tableView cellForRowAtIndexPath:index];
    textField = (UITextField *)[cell viewWithTag:50000];
    [textField resignFirstResponder];

    index = [NSIndexPath indexPathForRow:0 inSection:3];
    cell = [tableView cellForRowAtIndexPath:index];
    textField = (UITextField *)[cell viewWithTag:100];
    [textField resignFirstResponder];
    textField = (UITextField *)[cell viewWithTag:200];
    [textField resignFirstResponder];
    
}

-(void)chnageTextFild:(UITextField *)textField{
    if (textField.tag ==100) {  //联系人
        if(textField.text.length>11){
            textField.text = [textField.text substringToIndex:11];
        }
        self.bookingName = textField.text;
    }else if (textField.tag ==200){//手机号
        if(textField.text.length>11){
            textField.text = [textField.text substringToIndex:11];
            [textField resignFirstResponder];
        }
        self.mobileNbr = textField.text;
    }
    else if (textField.tag ==60000){//商家红包
        if([textField.text floatValue]>[self.shopBonus floatValue]){
            textField.text = self.shopBonus;
        }
        self.useshopBonus = textField.text;
    }else if (textField.tag ==50000){//惠圈红包
        if([textField.text floatValue]>[self.platBonus floatValue]){
            textField.text = self.platBonus;
        }
        self.useplatBonus = textField.text;
    }
    
    self.allUseBonus = [self.useplatBonus floatValue]+[self.useshopBonus floatValue];
    self.orderAmount = [NSString stringWithFormat:@"%0.2f",self.countMoney-self.allUseBonus];
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:0 inSection:2];
    [self.myTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
    
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

-(void)clickCountButton:(int)tag row:(int)row{
    NSString *str = [self.countS objectAtIndex:row];
    //tag =1 减 tag=2加
    if (tag ==1) {
        if ([str intValue]<=0) {
            str=@"0";
        }else{
            str = [NSString stringWithFormat:@"%d",[str intValue]-1];
        }
    }else{
         str = [NSString stringWithFormat:@"%d",[str intValue]+1];
    }
    
    [self.countS replaceObjectAtIndex:row withObject:str];
    
    
    float count = 0;
    for (int i=0; i<self.feeScaleS.count; i++) {
        
        NSDictionary *dic = [self.feeScaleS objectAtIndex:i];
        NSString *str = [self.countS objectAtIndex:i];
        
        NSString *price = [NSString stringWithFormat:@"%@",[dic objectForKey:@"price"]];
        count = [price floatValue]*[str integerValue]+count;
        
    }
    self.countMoney = count;
    
    self.orderAmount = [NSString stringWithFormat:@"%0.2f",self.countMoney- self.allUseBonus];
    
     NSIndexPath *indexPath=[NSIndexPath indexPathForRow:row inSection:1];
    [self.myTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
    
       indexPath=[NSIndexPath indexPathForRow:0 inSection:2];
    [self.myTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
}


#pragma mark --获取活动信息--
-(void)getInfoPreActInfo{
    
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.activityCode forKey:@"actCode"];
    
    
    NSString* vcode = [gloabFunction getSign:@"getInfoPreActInfo" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getInfoPreActInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSDictionary *userBonusInfo = [responseObject objectForKey:@"userBonusInfo"];
        weakSelf.shopBonus = [NSString stringWithFormat:@"%@",[userBonusInfo objectForKey:@"shopBonus"]];
        weakSelf.platBonus = [NSString stringWithFormat:@"%@",[userBonusInfo objectForKey:@"platBonus"]];
        
         NSDictionary *actInfo = [responseObject objectForKey:@"actInfo"];
        weakSelf.shopCode = [actInfo objectForKey:@"shopCode"];
        weakSelf.txtContent = [actInfo objectForKey:@"txtContent"];
        [weakSelf.feeScaleS addObjectsFromArray:[actInfo objectForKey:@"feeScale"]];
        for (NSDictionary *dic in [actInfo objectForKey:@"feeScale"]) {
            [weakSelf.countS addObject:@"0"];
        }
 
        
        [weakSelf.myTableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
    }];

    
}


#pragma mark --使用惠支付--
-(void)buyAct{
    
    [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];

    if (self.bookingName.length==0) {
        [SVProgressHUD showErrorWithStatus:@"请输入预定人姓名"];
        return ;
    }
    
    
    [self initJsonPrcClient:@"2"];
    
    NSMutableArray *orderInfo = [[NSMutableArray alloc]init];
    for (int i=0; i<self.feeScaleS.count; i++) {
        NSDictionary *feeScale = [self.feeScaleS objectAtIndex:i];
        NSMutableDictionary *dic = [[NSMutableDictionary alloc]init];
        [dic setObject:[NSString stringWithFormat:@"%@",[feeScale objectForKey:@"id"]] forKey:@"id"];
        [dic setObject:[self.countS objectAtIndex:i] forKey:@"nbr"];
        [dic setObject:[NSString stringWithFormat:@"%@",[feeScale objectForKey:@"price"]] forKey:@"price"];
        [orderInfo addObject:dic];
    }
    
   
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.activityCode forKey:@"actCode"];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.bookingName forKey:@"bookingName"];
    [params setObject:self.mobileNbr.length>0?self.mobileNbr:@"" forKey:@"mobileNbr"];
    [params setObject:[NSString stringWithFormat:@"%0.2f",self.countMoney] forKey:@"orderAmount"];
    [params setObject:self.useplatBonus forKey:@"platBonus"];
    [params setObject:self.useshopBonus forKey:@"shopBonus"];
    [params setObject:orderInfo forKey:@"orderInfo"];
    

    NSString* vcode = [gloabFunction getSign:@"submitActOrder" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"submitActOrder" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if([responseObject objectForKey:@"code"]){
            NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
            if ([code intValue] == 50000) {
                BMSQ_PayCardViewController *vc = [[BMSQ_PayCardViewController alloc]init];
                
                vc.fromVC =(int)self.navigationController.viewControllers.count-1;
                vc.shopCode = self.shopCode;
                vc.payType = @"1";   //支付方式
                vc.shopName = @"活动报名";
                vc.isTakeout = NO;
                vc.isdining = NO;
                vc.isBuy = NO;
                vc.type56 = YES;
                vc.consumeCode = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"consumeCode"]];
                vc.orderNbr =[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"orderNbr"]] ;
                vc.payNewPrice = [NSString stringWithFormat:@"%@元",[responseObject objectForKey:@"realPay"]];
                [weakSelf.navigationController pushViewController:vc animated:YES];
            }else{
                NSLog(@"加入失败----->%@",code);
            }

        }else{
            NSLog(@"加入失败----->%@",responseObject);
        }
        
        
     
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
    }];
    
}
@end
