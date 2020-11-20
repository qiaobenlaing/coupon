//
//  BMSQ_MemberCartEditViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/29.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MemberCardEditViewController.h"

@interface BMSQ_MemberCardEditViewController ()

@end

@implementation BMSQ_MemberCardEditViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.navigationItem setTitle:@"会员卡规则设定"];
    [self createModel];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}


- (void)createModel
{
    if(!_inputDic)
        _inputDic = [[NSMutableDictionary alloc] init];
}

#pragma mark -Delegate

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 5;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row==3)
        return 65.f;
    return 44.f;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    if(indexPath.row==3)
        cellIdentifier = @"EditTableViewCell";
    else if(indexPath.row==0)
        cellIdentifier = @"MemberTableViewCell1";
    else
        cellIdentifier = @"MemberTableViewCell";
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }else{
        
    }
    NSDictionary *dic = [self.cardDataArray objectAtIndex:[self.level intValue]];
    if(indexPath.row==0){
        UILabel *titleLabel = (UILabel *)[setCell viewWithTag:100];
        NSString *cStr = [[dic objectForKey:@"cardLvl"] stringByReplacingOccurrencesOfString:@"1" withString:@"一"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"2" withString:@"二"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"3" withString:@"三"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"4" withString:@"四"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"5" withString:@"五"];
        titleLabel.text = [NSString stringWithFormat:@"第%@级",cStr];

        _contentTextFiled = (UITextField *)[setCell viewWithTag:101];
        _contentTextFiled.text = [dic objectForKey:@"cardName"];
        _contentTextFiled.textAlignment = NSTextAlignmentRight;
        _contentTextFiled.enabled = NO;
        _contentTextFiled.font = [UIFont systemFontOfSize:15.f];
        UILabel *tipLabel = (UILabel *)[setCell viewWithTag:102];
        tipLabel.hidden = YES;
        
    }else if(indexPath.row==1){
        UILabel *titleLabel = (UILabel *)[setCell viewWithTag:100];
        titleLabel.text = @"需要积分";
        
        _scoreTextField = (UITextField *)[setCell viewWithTag:101];
        _scoreTextField.text = [NSString stringWithFormat:@"%d",[[dic objectForKey:@"discountRequire"] intValue]];
        _scoreTextField.keyboardType = UIKeyboardTypeDecimalPad;
        UILabel *tipLabel = (UILabel *)[setCell viewWithTag:102];
        tipLabel.text = @"分";
    }else if(indexPath.row==2){
        UILabel *titleLabel = (UILabel *)[setCell viewWithTag:100];
        titleLabel.text = @"享受折扣";
        
        _discountTextField = (UITextField *)[setCell viewWithTag:101];
        _discountTextField.text = [NSString stringWithFormat:@"%2d",[[dic objectForKey:@"discount"] intValue]];
        _discountTextField.keyboardType = UIKeyboardTypeDecimalPad;
        UILabel *tipLabel = (UILabel *)[setCell viewWithTag:102];
        tipLabel.text = @"折";
    }else if(indexPath.row==3){
        
        _eachYuanTextField = (UITextField *)[setCell viewWithTag:202];
        _eachYuanTextField.text = [NSString stringWithFormat:@"%.2f",[[dic objectForKey:@"pointsPerCash"] floatValue]];
        _eachYuanTextField.keyboardType = UIKeyboardTypeDecimalPad;
        _eachYuanScoreTextFiled = (UITextField *)[setCell viewWithTag:201];
        _eachYuanScoreTextFiled.text = @"1";
        _eachYuanScoreTextFiled.enabled= NO;

        _eachScoreTextField = (UITextField *)[setCell viewWithTag:201];
        _eachScoreYuanTextField = (UITextField *)[setCell viewWithTag:201];
        
        [setCell setBackgroundColor:UICOLOR(240, 239, 245, 1)];

    }else if(indexPath.row==4){
        UILabel *titleLabel = (UILabel *)[setCell viewWithTag:100];
        titleLabel.text = @"积分有效期";
        
        _expireMonthTextField = (UITextField *)[setCell viewWithTag:101];
        _expireMonthTextField.text = [NSString stringWithFormat:@"%d",[[dic objectForKey:@"pointLifeTime"] intValue]];
        _expireMonthTextField.keyboardType = UIKeyboardTypeDecimalPad;
        UILabel *tipLabel = (UILabel *)[setCell viewWithTag:102];
        tipLabel.text = @"月";
    }
    setCell.selectionStyle = UITableViewCellSelectionStyleNone;
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    
    for(int i=0;i<2;i++){
        UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(15+(i*(tableView.frame.size.width-45)/2+i*10),27,(tableView.frame.size.width-45)/2,44)];
        btn.backgroundColor = [UIColor colorWithHexString:@"0xC5000A"];
        btn.layer.cornerRadius = 4.0f;
        btn.titleLabel.font = [UIFont systemFontOfSize:17.f];
        if(i==0){
            [btn setTitle:NSLocalizedString(@"上一步", @"上一步") forState:UIControlStateNormal];
            [btn setTitle:NSLocalizedString(@"上一步", @"上一步") forState:UIControlStateHighlighted];
            [btn addTarget:self action:@selector(backBtnClicked) forControlEvents:UIControlEventTouchUpInside];
        }else {
            [btn setTitle:NSLocalizedString(@"下一步", @"下一步") forState:UIControlStateNormal];
            [btn setTitle:NSLocalizedString(@"下一步", @"下一步") forState:UIControlStateHighlighted];
            [btn addTarget:self action:@selector(setupBtnClicked) forControlEvents:UIControlEventTouchUpInside];
        }
        btn.showsTouchWhenHighlighted = YES;
        [v addSubview:btn];
    }
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 100.f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 18.f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}
                                            
- (void)backBtnClicked
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)setupBtnClicked
{
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    NSLog(@"UD%@",[uD objectForKey:@"USERINFO"]);
    [self.view endEditing:YES];
    
    NSDictionary *dic = [self.cardDataArray objectAtIndex:[self.level intValue]];

    [_inputDic setObject:@"1000" forKey:@"cardType"];
    [_inputDic setObject:[dic objectForKey:@"cardLvl"] forKey:@"cardLvl"];
    [_inputDic setObject:[gloabFunction getStaffCode] forKey:@"creatorCode"];
    [_inputDic setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [_inputDic setObject:@"" forKey:@"url"];

    
    NSString *userStr = [_contentTextFiled.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入卡名");
        return;
    }else{
        [_inputDic setObject:_contentTextFiled.text forKey:@"cardName"];
    }
    

    
    userStr = [_scoreTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入需要积分");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"discountRequire"];
    }
    
    userStr = [_discountTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入可享受折扣");
        return;
    }else{
        /*正整数*/
        [_inputDic setObject:userStr forKey:@"discount"];
    }
    
    userStr = [_expireMonthTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入积分有效期");
        return;
    }else{
        /*正整数*/
        [_inputDic setObject:userStr forKey:@"pointLifetime"];
    }
    
    userStr = [_eachYuanTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    NSString *pStr = [_eachYuanScoreTextFiled.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];

    if(!userStr||userStr.length==0){
        CSAlert(@"请输入每xx元");
        return;
    }else{
        if(!pStr||pStr.length==0){
            CSAlert(@"请输入积xx分");
            return;
        }
        /*正整数*/
        [_inputDic setObject:[NSString stringWithFormat:@"%d",[userStr intValue]]forKey:@"pointsPerCash"];
    }
    
    [_inputDic setObject:[NSString stringWithFormat:@"%d",0] forKey:@"outPointsPerCash"];

//    userStr = [_eachScoreTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
//    pStr = [_eachScoreYuanTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
//    
//    if(!userStr||userStr.length==0){
//        CSAlert(@"请输入每xx分");
//        return;
//    }else{
//        if(!pStr||pStr.length==0){
//            CSAlert(@"请输入值xx元");
//            return;
//        }
//        /*正整数*/
//        [_inputDic setObject:[NSString stringWithFormat:@"%d",[userStr intValue]/[pStr intValue]] forKey:@"outPointsPerCash"];
//    }
    
    [self httpRequest];
}

- (void)httpRequest
{
    NSString* vcode = [gloabFunction getSign:@"editCard" strParams:[_inputDic objectForKey:@"cardName"]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    [_inputDic setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [_inputDic setObject:vcode forKey:@"vcode"];
    [_inputDic setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"editCard" withParameters:_inputDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([responseObject objectForKey:@"code"])
            {
                if([self.level intValue]==self.cardDataArray.count-1){
                    [self.navigationController popToRootViewControllerAnimated:YES];
                }else{
                    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_BenefitCard" bundle:nil];
                    BMSQ_MemberCardEditViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_MemberCardEdit"];
                    vc.cardDataArray = self.cardDataArray;
                    vc.level = [NSString stringWithFormat:@"%d",[self.level intValue]+1];
                    
                    [self.navigationController pushViewController:vc animated:YES];
                }
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}
@end
