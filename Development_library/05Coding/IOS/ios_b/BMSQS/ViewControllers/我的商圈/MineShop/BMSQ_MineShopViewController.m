//
//  BMSQ_MineShopViewController.m
//  BMSQS
//
//  Created by gh on 15/10/21.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_MineShopViewController.h"
#import "UIImageView+WebCache.h"
#import "Coupon_ListTableViewCell.h"
#import "BMSQ_PhotoViewController.h"


#import "BMSQ_CouponReceiveViewController.h"

@interface BMSQ_MineShopViewController ()<Coupon_ListTableViewCellDelegate>

@property (nonatomic, strong)UITableView *shopDetail_TableView;
@property (nonatomic, strong)NSMutableArray *shopProductPhoto;

@property (nonatomic, strong)NSMutableArray *shopPicArray;
@property (nonatomic, strong)NSDictionary *shopInfo;

@property (nonatomic, strong)NSMutableArray *myCouponArray;
@property (nonatomic, strong)NSMutableArray *myActArray;


@property (nonatomic, assign)BOOL isShowMyCoupon;
@property (nonatomic, assign)BOOL isShowAct;

@property (nonatomic,assign)float shopDeatilH;


@end

@implementation BMSQ_MineShopViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    

    [self setNavBackItem];
    [self setNavTitle:@"我的店铺"];
    
    self.shopProductPhoto = [[NSMutableArray alloc]init];  //顶部图片
    self.shopPicArray = [[NSMutableArray alloc]init]; //商店图片
    self.myCouponArray = [[NSMutableArray alloc]init]; //商店发布优惠券
    self.myActArray = [[NSMutableArray alloc]init];  //商店活动
    self.shopDeatilH = 60;

    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [btnback setTitle:@"装修" forState:0];
    btnback.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    [btnback addTarget:self action:@selector(clickRight:) forControlEvents:UIControlEventTouchUpInside];
//    UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btnback];
//    self.navigationItem.rightBarButtonItem = backButtonItem;
    [self setNavRightBarItem:btnback];

    self.shopDetail_TableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.shopDetail_TableView.backgroundColor = [UIColor clearColor];
    self.shopDetail_TableView.delegate = self;
    self.shopDetail_TableView.dataSource = self;
    self.shopDetail_TableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    [self.view addSubview:self.shopDetail_TableView];
    [self sGetShopInfo];

    
}
-(void)SetHeadView:(NSArray *)imageArray shopImageUrl:(NSString *)shopImageUrl{
    
    UIView *bgHeadView =[[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/3+30+10)];
    bgHeadView.backgroundColor = [UIColor clearColor];
    UIView *HeadView =[[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/3+30)];
    [bgHeadView addSubview:HeadView];
    HeadView.backgroundColor = [UIColor whiteColor];
    UIScrollView *HeadScroller = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH,APP_VIEW_HEIGHT/3-30)];
    HeadScroller.pagingEnabled = YES;
    HeadScroller.showsHorizontalScrollIndicator = YES;
    HeadScroller.showsVerticalScrollIndicator = NO;
    for ( int i = 0;i<imageArray.count;i++ ) {
        
        NSDictionary *dic = [imageArray objectAtIndex:i];
        
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0,APP_VIEW_WIDTH,APP_VIEW_HEIGHT/3-30)];
        NSString *imageName = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"imgUrl"]];
        [imageView sd_setImageWithURL:[NSURL URLWithString:imageName] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
        //关键步骤 设置可变化背景view属性
        imageView.autoresizingMask = UIViewAutoresizingFlexibleHeight| UIViewAutoresizingFlexibleWidth;
        imageView.clipsToBounds = YES;
        imageView.contentMode = UIViewContentModeScaleAspectFill;
        imageView.tag = 100;
        [HeadScroller addSubview:imageView];
    }
    [HeadView addSubview:HeadScroller];
    
    UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(10,HeadScroller.frame.size.height, HeadScroller.frame.size.width-110, 30)];
    nameLabel.backgroundColor = [UIColor whiteColor];
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    NSString *shopName = [uD objectForKey:@"shopName"];
    nameLabel.text =shopName;
    nameLabel.font = [UIFont systemFontOfSize:17.f];
    [HeadView addSubview:nameLabel];
    
    
    UIImageView *headImageView = [[UIImageView alloc]initWithFrame:CGRectMake(HeadScroller.frame.size.width-75,HeadScroller.frame.size.height-30, 70, 70)];
    
    [headImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,shopImageUrl]] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    headImageView.layer.borderWidth = 0.4;
    headImageView.layer.borderColor = [[UIColor grayColor]CGColor];
    headImageView.layer.masksToBounds = YES;
    headImageView.layer.cornerRadius = 3;
    [HeadView addSubview:headImageView];
    
    self.shopDetail_TableView.tableHeaderView = bgHeadView;
    
    
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        static NSString *identfier = @"shopDetailCellSection0";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identfier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identfier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor whiteColor];
            
            UILabel *contentLabel = [[UILabel alloc]initWithFrame:CGRectMake(40, 0, APP_VIEW_WIDTH-40, 40)];
            contentLabel.backgroundColor = [UIColor whiteColor];
            contentLabel.font = [UIFont systemFontOfSize:14.f];
            contentLabel.tag = 100;
            contentLabel.numberOfLines = 2;
            [cell.contentView addSubview:contentLabel];
            
            UIImageView *iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(15, 0,12, 16)];
            iconImage.backgroundColor = [UIColor clearColor];
            iconImage.center = CGPointMake(20, 20);
            iconImage.tag = 99;
            [cell.contentView addSubview:iconImage];
            
            UIView *bottomView = [[UIView alloc]initWithFrame:CGRectMake(15, 38, APP_VIEW_WIDTH-30, 0.5)];
            bottomView.backgroundColor = [UIColor colorWithRed:235/255.0 green:233/255.0 blue:241/255.0 alpha:1];
            bottomView.hidden = YES;
            bottomView.tag = 111;
            [cell.contentView addSubview:bottomView];
            
            
            
            UIScrollView *scrollerView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH-40, 70)];
            scrollerView.backgroundColor = [UIColor whiteColor];
            scrollerView.hidden = YES;
            scrollerView.tag = 222;
            [cell.contentView addSubview:scrollerView];
            
            UIButton *nextButton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-40, 10, 40, 70)];
            nextButton.backgroundColor = [UIColor whiteColor];
            [nextButton setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
            nextButton.hidden = YES;
            nextButton.tag = 333;
//            [nextButton addTarget:self action:@selector(gotoPhontVC:) forControlEvents:UIControlEventTouchUpInside];
            [cell.contentView addSubview:nextButton];
            
        }
        UIImageView *iconImage = (UIImageView *)[cell viewWithTag:99];
        UILabel *label = (UILabel *)[cell viewWithTag:100];
        UIView *lineView = (UIView *)[cell viewWithTag:111];
        UIScrollView *sc = (UIScrollView *)[cell viewWithTag:222];
        UIButton *button = (UIButton *)[cell.contentView viewWithTag:333];
        if (indexPath.row ==0) {
            lineView.hidden = NO;
            label.hidden = NO;
            iconImage.hidden = NO;
            button.hidden = YES;
            [iconImage setImage:[UIImage imageNamed:@"address"]];
            sc.hidden = YES;
            
            
            if ([self.shopInfo objectForKey:@"street"]) {
                label.text = [NSString stringWithFormat:@"%@",[self.shopInfo objectForKey:@"street"]] ;
                
            }else{
                
            }
            
            
        }else if (indexPath.row ==1){
            lineView.hidden = NO;
            label.hidden = NO;
            iconImage.hidden = NO;
            [iconImage setImage:[UIImage imageNamed:@"telephone"]];
            button.hidden = YES;
            sc.hidden = YES;
            
            if ([self.shopInfo objectForKey:@"tel"]) {
                label.text = [NSString stringWithFormat:@"%@",[self.shopInfo objectForKey:@"tel"]] ;
            }
            
        } else if (indexPath.row ==2){
            lineView.hidden = YES;
            label.hidden = YES;
            iconImage.hidden = YES;
            sc.hidden = NO;
            button.hidden = NO;
            sc.contentSize = CGSizeMake(self.shopPicArray.count*60+(self.shopPicArray.count +1)*15, 70);
            
            for (int i = 0; i<self.shopPicArray.count; i++) {
                UIImageView *imageV = [[UIImageView alloc]initWithFrame:CGRectMake(i*60+(i+1)*15, 5, 60, 60)];
                [imageV sd_setImageWithURL:[NSURL URLWithString:[self.shopPicArray objectAtIndex:i]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
                imageV.backgroundColor = [UIColor clearColor];
                imageV.tag = i;
                [sc addSubview:imageV];
                
//                UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(showPhothSC:)];
//                [imageV addGestureRecognizer:tap];
//                imageV.userInteractionEnabled = YES;
                
            }

        }
        
        return cell;

    }
    else if (indexPath.section == 1){
        static NSString *identifier = @"Coupon_ListTableViewCell";
        Coupon_ListTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];

        if (cell ==nil) {
            cell = [[Coupon_ListTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.listDelegate = self;
        }
        cell.hiddenBtn = YES;
        [cell setListCoupon:[self.myCouponArray objectAtIndex:indexPath.row]];
        return cell;
        
    }
    
    else if (indexPath.section == 2){
        static NSString *identifier = @"MineShop_Shopdetails_3";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, self.shopDeatilH+45)];
            bgView.tag = 3001;
            bgView.backgroundColor = [UIColor whiteColor];
            [cell.contentView addSubview:bgView];
            
            UILabel *actLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
            actLabel.tag = 100;
            actLabel.font = [UIFont systemFontOfSize:15.f];
            actLabel.text = @"    店铺简介";
            [bgView addSubview:actLabel];
            
            UILabel *DeatilLabel = [[UILabel alloc]init];
            DeatilLabel.tag = 101;
            DeatilLabel.numberOfLines = 0;
            DeatilLabel.backgroundColor = [UIColor whiteColor];
            DeatilLabel.font = [UIFont systemFontOfSize:15.f];
            [bgView addSubview:DeatilLabel];
            
        }
        
        
//        CGSize size = [[self.shopInfo objectForKey:@"shortDes"]boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-60, MAXFLOAT)
//                                                                            options:NSStringDrawingUsesLineFragmentOrigin
//                                                                         attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:15.f]}
//                                                                            context:nil].size;
        
        UIView *bgView = [cell.contentView viewWithTag:3001];
        bgView.frame = CGRectMake(0, 5, APP_VIEW_WIDTH, self.shopDeatilH + 45);
        
        UILabel *contentlabel = (UILabel *)[cell.contentView viewWithTag:101];
        contentlabel.frame = CGRectMake(30, 45, APP_VIEW_WIDTH-60, self.shopDeatilH);
        
        NSString *str = [self.shopInfo objectForKey:@"shortDes"];
        if (str.length ==0) {
            contentlabel.text = @"暂无简介";
        }else{
            contentlabel.text = [self.shopInfo objectForKey:@"shortDes"];
        }
        
        return cell;

        
    }

    
    static NSString *identifier = @"asdf";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSLog(@"index%@",indexPath);
    if (indexPath.section == 1) {
        NSLog(@"优惠券");
        
    }
    
}


-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if(indexPath.section == 0){
        
        if (indexPath.row ==2) {
            return 80;
        }else{
            return 40;
        }
        
    }else if (indexPath.section ==1){
        
        if (self.isShowMyCoupon ) {
            return APP_VIEW_HEIGHT/6+10;
        }else{
            return 0;
        }
       
    }else if (indexPath.section == 2){
        
        return self.shopDeatilH + 45+5;
        
    }
    return 20;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section == 0) {
        if(self.shopPicArray.count >0){
            return 3;
        }else{
            return 2;
        }
    }else if (section == 1){  //发过优惠券
        
        if (self.isShowMyCoupon ) {
            return self.myCouponArray.count;
        }else{
            return 0;
        }
    }else if (section == 2){
        return 1;
       
    }
    return 0;
 
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    
    if (section ==1) {
        
        if (self.myCouponArray.count >0) {
            return 50;
        }else{
            return 0;
        }
        
    }
    return 0;
    
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    UIView *sectionView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 50)];
    sectionView.backgroundColor = [UIColor colorWithRed:235/255.0 green:233/255.0 blue:241/255.0 alpha:1];
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0,5, APP_VIEW_WIDTH, 45)];
    label.backgroundColor = [UIColor whiteColor];
    label.font = [UIFont systemFontOfSize:15.f];
    label.textColor = [UIColor colorWithRed:26/255.0 green:26/255.0 blue:26/255.0 alpha:1];
    [sectionView addSubview:label];
    
    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 45)];
    button.backgroundColor = [UIColor clearColor];
    button.imageEdgeInsets = UIEdgeInsetsMake(0, APP_VIEW_WIDTH-40, 0, 0);
    
    [button setImage:[UIImage imageNamed:@"garydown"] forState:UIControlStateNormal];
    [button setImage:[UIImage imageNamed:@"garyup"] forState:UIControlStateSelected];
    [button addTarget:self action:@selector(showCoupon:) forControlEvents:UIControlEventTouchUpInside];
    [sectionView addSubview:button];
    
    if (section ==1) {
        
        if (self.myCouponArray.count>0) {
            label.text = @"    我发布的优惠券";
            button.tag = 100;
            button.selected = self.isShowMyCoupon;
            
            return sectionView;
            
        }else{
            return nil;
            
        }

    }
        
    else {
        return nil;
    }
    
}



-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 3;
}

-(void)showCoupon:(UIButton *)button{
    int i = (int)button.tag;
   
    if (i == 100) {
        self.isShowMyCoupon = !self.isShowMyCoupon;
//        if (button.selected) {
//            self.isShowMyCoupon = YES;
//            
//        }else{
//            self.isShowMyCoupon = NO;
//
//        }
        NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:1];
        [self.shopDetail_TableView  reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
        
    }
    
}


-(void)sGetShopInfo{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"sGetShopInfo" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"sGetShopInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if ([responseObject objectForKey:@"shopInfo"]) {
            
            NSArray *picArrty = [responseObject objectForKey:@"shopPhotoList"];
            for (NSDictionary *dic in picArrty) {
                NSString *imageName = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"url"]];
                [weakSelf.shopProductPhoto addObject:imageName];
            }
            weakSelf.myActArray = [responseObject objectForKey:@"actList"];
            
            weakSelf.shopInfo = [responseObject objectForKey:@"shopInfo"];
            NSString *str = [weakSelf.shopInfo objectForKey:@"shortDes"];
            NSDictionary *attributes = @{NSFontAttributeName: [UIFont systemFontOfSize:15.f]};
            CGSize contentSize=[str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-60, MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attributes context:nil].size;
            
           
            self.shopDeatilH = contentSize.height<45?45:contentSize.height;
            
            NSDictionary *shopProductPhoto = [responseObject objectForKey:@"shopProductPhoto"];
            for (NSDictionary *dic in shopProductPhoto) {
                [weakSelf.shopPicArray addObject:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"url"]]];
            }
            weakSelf.myCouponArray = [responseObject objectForKey:@"shopCoupon"];
            [weakSelf SetHeadView:[responseObject objectForKey:@"shopDecoration"] shopImageUrl:[weakSelf.shopInfo objectForKey:@"logoUrl"]];
            [weakSelf.shopDetail_TableView reloadData];
        }
        
        
        
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [SVProgressHUD dismiss];
    }];

    
    
}




#pragma mark  --装修
-(void)clickRight:(UIButton *)button{
    BMSQ_PhotoViewController *vc = [[BMSQ_PhotoViewController alloc] init];
    vc.shopCode = [gloabFunction getShopCode];
    [self.navigationController pushViewController:vc animated:YES];
    
    
}

@end
